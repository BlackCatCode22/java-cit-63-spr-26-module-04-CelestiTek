public class Animal {
    // Private fields (encapsulation)
    private String name;
    private int age;
    private String species;

    // Constructor
    public Animal(String name, int age, String species) {
        setName(name);
        setAge(age);
        setSpecies(species);
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name with validation
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name.trim();
    }

    // Getter for age
    public int getAge() {
        return age;
    }

    // Setter for age with validation
    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
        this.age = age;
    }

    // Getter for species
    public String getSpecies() {
        return species;
    }

    // Setter for species with validation
    public void setSpecies(String species) {
        if (species == null || species.trim().isEmpty()) {
            throw new IllegalArgumentException("Species cannot be empty.");
        }
        this.species = species.trim();
    }

    // Method to display animal info
    public void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age + ", Species: " + species);
    }

    // Main method for demonstration
    public static void main(String[] args) {
        try {
            Animal dog = new Animal("Buddy", 5, "Dog");
            dog.displayInfo();

            // Using setters
            dog.setAge(6);
            dog.setName("Max");
            dog.displayInfo();

            // Example of validation error
            // dog.setAge(-2); // Uncomment to test exception

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
