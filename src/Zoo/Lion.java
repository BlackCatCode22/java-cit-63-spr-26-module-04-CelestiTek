package Zoo;

public class Lion extends Animal {
    // Create a static int that keeps track of the number of lions created.
    private static int numOfLions = 0;

    // Public static getter for the number of lions created.
    public static int getNumOfLions() {
        return numOfLions;
    }

    // Create a constructor.
    public Lion() {
        super();
        numOfLions++;
    }

    // Create a constructor that will have all the fields I want.
    public Lion(String sex, int age, int weight, String animalName,
                String animalID, String animalBirthDate, String animalColor,
                String animalOrigin, String animalArrivalDate) {
        super(sex, age, weight, animalName, animalID, animalBirthDate, animalColor, animalOrigin, animalArrivalDate);
        numOfLions++;
    }
}
