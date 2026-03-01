package Zoo;

public class Hyena extends Animal {
    // Create a static int that keeps track of the number of hyenas created.
    private static int numOfHyenas = 0;

    // Public static getter for the number of hyenas created.
    public static int getNumOfHyenas() {
        return numOfHyenas;
    }

    // Create a constructor.
    public Hyena() {
        super();
        numOfHyenas++;
    }

    // Create a constructor that will have all the fields I want.
    public Hyena(String sex, int age, int weight, String animalName,
                 String animalID, String animalBirthDate, String animalColor,
                 String animalOrigin, String animalArrivalDate) {
        super(sex, age, weight, animalName, animalID, animalBirthDate, animalColor, animalOrigin, animalArrivalDate);
        numOfHyenas++;
    }
}