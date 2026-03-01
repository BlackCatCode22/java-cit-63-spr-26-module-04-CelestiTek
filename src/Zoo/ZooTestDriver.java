package Zoo;

/**
 * Simple driver to exercise Animal getters/setters and subclass static counters.
 * This also prevents IDE "never used" warnings for basic methods.
 */
public class ZooTestDriver {
    public static void main(String[] args) {
        // Create one of each animal using the full-fields constructor.
        Hyena h1 = new Hyena("female", 4, 120, "Giggly", "Hy01", "02-14-2022",
                "spotted", "from Safari Park", "2026-02-28");
        Bear b1 = new Bear("male", 7, 500, "Bruno", "Be01", "05-10-2019",
                "brown", "from Yellowstone", "2026-02-28");
        Lion l1 = new Lion("male", 5, 420, "Simba", "Li01", "08-01-2020",
                "golden", "from Serengeti", "2026-02-28");
        Tiger t1 = new Tiger("female", 3, 310, "Shere Khan", "Ti01", "11-20-2022",
                "orange", "from Sundarbans", "2026-02-28");

        // Use a bunch of Animal getters.
        System.out.println(h1.getAnimalID() + ": " + h1.getAnimalName() + ", " + h1.getSex());
        System.out.println(b1.getAnimalID() + ": " + b1.getAnimalName() + ", age " + b1.getAge());
        System.out.println(l1.getAnimalID() + ": " + l1.getAnimalName() + ", weight " + l1.getWeight());
        System.out.println(t1.getAnimalID() + ": " + t1.getAnimalName() + ", color " + t1.getAnimalColor());

        // Use setters too.
        h1.setAnimalOrigin("from Updated Zoo");
        h1.setAnimalArrivalDate("2026-03-01");
        System.out.println("Updated origin: " + h1.getAnimalOrigin() + ", arrival: " + h1.getAnimalArrivalDate());

        // Use subclass static counters.
        System.out.println("Hyenas created: " + Hyena.getNumOfHyenas());
        System.out.println("Bears created: " + Bear.getNumOfBears());
        System.out.println("Lions created: " + Lion.getNumOfLions());
        System.out.println("Tigers created: " + Tiger.getNumOfTigers());

        // Also exercise the default constructors.
        new Hyena();
        new Bear();
        new Lion();
        new Tiger();

        System.out.println("Total animals created (Animal.numOfAnimals): " + Animal.numOfAnimals);
        System.out.println("Hyenas created after defaults: " + Hyena.getNumOfHyenas());
    }
}

