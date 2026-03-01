package Zoo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Reads arrivingAnimals.txt + animalNames.txt and creates a report newAnimals.txt.
 *
 * Report requirements:
 * - list animals by species
 * - include name + age per animal
 * - include total count per species
 */
public class NewAnimalsReport {

    private enum Species {
        HYENA("Hyena"),
        LION("Lion"),
        BEAR("Bear"),
        TIGER("Tiger");

        final String title;

        Species(String title) {
            this.title = title;
        }

        static Species fromLine(String line) {
            String lower = line.toLowerCase(Locale.ROOT);
            if (lower.contains("hyena")) return HYENA;
            if (lower.contains("lion")) return LION;
            if (lower.contains("bear")) return BEAR;
            if (lower.contains("tiger")) return TIGER;
            throw new IllegalArgumentException("Unknown species in line: " + line);
        }
    }

    // Example: "4 year old female hyena, born in spring, tan color, 70 pounds, from ..."
    private static final Pattern ARRIVAL_PATTERN = Pattern.compile("^(\\d+)\\s+year\\s+old\\s+(male|female)\\s+(hyena|lion|tiger|bear),\\s*(.*)$",
            Pattern.CASE_INSENSITIVE);

    private static final Pattern WEIGHT_PATTERN = Pattern.compile("(\\d+)\\s*pounds", Pattern.CASE_INSENSITIVE);
    private static final Pattern COLOR_PATTERN = Pattern.compile("([\\w\\s&-]+?)\\s+color", Pattern.CASE_INSENSITIVE);
    private static final Pattern BORN_PATTERN = Pattern.compile("born in\\s+([\\w\\s-]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern FROM_PATTERN = Pattern.compile("from\\s+(.*)$", Pattern.CASE_INSENSITIVE);

    // collect parsing errors to include in the report
    private static final List<String> parseErrors = new ArrayList<>();

    static void main(String[] args) {
        try {
            Path cwd = Path.of(System.getProperty("user.dir"));
            Path moduleRoot = cwd.getParent() != null ? cwd.getParent() : cwd; // fallback if running from a root dir

            String arrivingFileName = "arrivingAnimals.txt";
            String namesFileName = "animalNames.txt";

            Path arrivingPathCandidate = args.length >= 1 ? Path.of(args[0]) : null;
            Path namesPathCandidate = args.length >= 2 ? Path.of(args[1]) : null;
            Path outputPath = args.length >= 3 ? Path.of(args[2]) : moduleRoot.resolve("newAnimals.txt");

            Path arrivingPath = resolveExistingPathWithSearch(arrivingPathCandidate, arrivingFileName, cwd, moduleRoot);
            Path namesPath = resolveExistingPathWithSearch(namesPathCandidate, namesFileName, cwd, moduleRoot);

            if (arrivingPath == null || namesPath == null) {
                System.err.println("Could not locate one or both input files. Please ensure " + arrivingFileName + " and " + namesFileName + " exist.");
                return;
            }

            Map<Species, List<String>> namesBySpecies = parseNamesFile(namesPath);
            List<Animal> arrivals = parseArrivals(arrivingPath, namesBySpecies);

            writeReport(outputPath, arrivals);
            System.out.println("Report written to: " + outputPath.toAbsolutePath());
        } catch (Exception e) {
            System.err.println("Unhandled exception while running NewAnimalsReport: " + e);
            e.printStackTrace(System.err);
        }
    }

    // Try explicit path first, then search under cwd and moduleRoot up to a small depth
    private static Path resolveExistingPathWithSearch(Path explicit, String fileName, Path cwd, Path moduleRoot) {
        if (explicit != null && Files.exists(explicit)) return explicit;

        // Check cwd directly
        Path p = cwd.resolve(fileName);
        if (Files.exists(p)) return p;

        // Check module root directly
        p = moduleRoot.resolve(fileName);
        if (Files.exists(p)) return p;

        // Search under cwd and moduleRoot up to depth 3
        Path found = findFileUnder(cwd, fileName, 3);
        if (found != null) return found;
        found = findFileUnder(moduleRoot, fileName, 3);
        return found;
    }

    private static Path findFileUnder(Path root, String fileName, int maxDepth) {
        try (Stream<Path> stream = Files.walk(root, maxDepth)) {
            return stream.filter(p -> p.getFileName().toString().equalsIgnoreCase(fileName)).findFirst().orElse(null);
        } catch (IOException e) {
            return null;
        }
    }

    private static Map<Species, List<String>> parseNamesFile(Path namesPath) throws IOException {
        List<String> lines = Files.readAllLines(namesPath, StandardCharsets.UTF_8);

        Map<Species, List<String>> namesBySpecies = new EnumMap<>(Species.class);
        for (Species s : Species.values()) namesBySpecies.put(s, new ArrayList<>());

        Species current = null;
        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) continue;

            String lower = line.toLowerCase(Locale.ROOT);
            if (lower.startsWith("hyena names")) {
                current = Species.HYENA;
                continue;
            }
            if (lower.startsWith("lion names")) {
                current = Species.LION;
                continue;
            }
            if (lower.startsWith("bear names")) {
                current = Species.BEAR;
                continue;
            }
            if (lower.startsWith("tiger names")) {
                current = Species.TIGER;
                continue;
            }

            if (current == null) continue;

            // Names line: comma-separated. Keep internal spaces (e.g., "Winnie the Pooh")
            for (String part : line.split(",")) {
                String name = part.trim();
                if (!name.isEmpty()) {
                    namesBySpecies.get(current).add(name);
                }
            }
        }

        return namesBySpecies;
    }

    private static List<Animal> parseArrivals(Path arrivingPath, Map<Species, List<String>> namesBySpecies) throws IOException {
        List<String> lines = Files.readAllLines(arrivingPath, StandardCharsets.UTF_8);

        // Track which name index we are at for each species.
        Map<Species, Integer> nextNameIndex = new EnumMap<>(Species.class);
        for (Species s : Species.values()) nextNameIndex.put(s, 0);

        // Track ID counters to create IDs like Hy01, Hy02
        Map<Species, Integer> idCounters = new EnumMap<>(Species.class);
        for (Species s : Species.values()) idCounters.put(s, 1);

        List<Animal> entries = new ArrayList<>();

        int lineno = 0;
        for (String raw : lines) {
            lineno++;
            String line = raw.trim();
            if (line.isEmpty()) continue;

            try {
                Matcher m = ARRIVAL_PATTERN.matcher(line);
                if (!m.matches()) {
                    // Skip malformed line but continue processing others
                    String msg = "Skipping malformed arrival line (" + lineno + "): " + line;
                    System.err.println(msg);
                    parseErrors.add(msg);
                    continue;
                }

                int age = Integer.parseInt(m.group(1));
                String sex = m.group(2).toLowerCase(Locale.ROOT);
                Species species = Species.fromLine(line);
                String rest = m.group(4);

                // weight
                int weight = 0;
                Matcher wm = WEIGHT_PATTERN.matcher(rest);
                if (wm.find()) {
                    try {
                        weight = Integer.parseInt(wm.group(1));
                    } catch (NumberFormatException ignored) {
                    }
                }

                // color
                String color = "";
                Matcher cm = COLOR_PATTERN.matcher(rest);
                if (cm.find()) color = cm.group(1).trim();

                // birth date (or born description)
                String birthDesc = "";
                Matcher bm = BORN_PATTERN.matcher(rest);
                if (bm.find()) birthDesc = bm.group(1).trim();

                // origin
                String origin = "";
                Matcher fm = FROM_PATTERN.matcher(rest);
                if (fm.find()) origin = fm.group(1).trim();

                // get a name from pool or fallback
                List<String> pool = namesBySpecies.get(species);
                int idx = nextNameIndex.get(species);
                String chosenName;
                if (idx < pool.size()) {
                    chosenName = pool.get(idx);
                } else {
                    // fallback generated name
                    chosenName = species.title + "-" + String.format("%02d", idx + 1);
                }
                nextNameIndex.put(species, idx + 1);

                // create animalID like Hy01
                int idNum = idCounters.get(species);
                String prefix;
                switch (species) {
                    case HYENA:
                        prefix = "Hy";
                        break;
                    case LION:
                        prefix = "Li";
                        break;
                    case BEAR:
                        prefix = "Be";
                        break;
                    case TIGER:
                        prefix = "Ti";
                        break;
                    default:
                        prefix = "Un";
                }
                String animalID = prefix + String.format("%02d", idNum);
                idCounters.put(species, idNum + 1);

                String arrivalDate = LocalDate.now().toString();
                String birthDate = birthDesc.isEmpty() ? "" : birthDesc;

                // instantiate the correct subclass using the all-fields constructor
                Animal a;
                switch (species) {
                    case HYENA:
                        a = new Hyena(sex, age, weight, chosenName, animalID, birthDate, color, origin, arrivalDate);
                        break;
                    case LION:
                        a = new Lion(sex, age, weight, chosenName, animalID, birthDate, color, origin, arrivalDate);
                        break;
                    case BEAR:
                        a = new Bear(sex, age, weight, chosenName, animalID, birthDate, color, origin, arrivalDate);
                        break;
                    case TIGER:
                        a = new Tiger(sex, age, weight, chosenName, animalID, birthDate, color, origin, arrivalDate);
                        break;
                    default:
                        throw new IllegalStateException("Unhandled species: " + species);
                }

                entries.add(a);
            } catch (Exception e) {
                String msg = "Error parsing arrival line (" + lineno + "): " + line + " -> " + e;
                System.err.println(msg);
                e.printStackTrace(System.err);
                parseErrors.add(msg + " -- " + e.toString());
            }
        }

        return entries;
    }

    private static void writeReport(Path outputPath, List<Animal> arrivals) throws IOException {
        // Group into an EnumMap for efficiency and stable key set.
        Map<Species, List<Animal>> grouped = new EnumMap<>(Species.class);
        for (Species s : Species.values()) grouped.put(s, new ArrayList<>());

        for (Animal a : arrivals) {
            // Determine species from animalID prefix or other heuristics; here we infer from class
            Species s;
            if (a instanceof Hyena) s = Species.HYENA;
            else if (a instanceof Lion) s = Species.LION;
            else if (a instanceof Bear) s = Species.BEAR;
            else if (a instanceof Tiger) s = Species.TIGER;
            else throw new IllegalStateException("Unknown animal subclass: " + a.getClass());

            grouped.get(s).add(a);
        }

        List<String> out = new ArrayList<>();
        out.add("New Animals Report");
        out.add("Generated: " + LocalDate.now());
        out.add("");

        for (Species species : Species.values()) {
            List<Animal> list = grouped.get(species);

            out.add(species.title + "s");
            out.add("-----------------");
            for (Animal a : list) {
                out.add(a.getAnimalName() + " - " + a.getAge() + " years old");
            }
            out.add("Total " + species.title + "s: " + list.size());
            out.add("");
        }

        if (!parseErrors.isEmpty()) {
            out.add("Errors encountered while parsing:");
            for (String e : parseErrors) out.add("- " + e);
            out.add("");
        }

        Files.write(outputPath, out, StandardCharsets.UTF_8);
    }
}

