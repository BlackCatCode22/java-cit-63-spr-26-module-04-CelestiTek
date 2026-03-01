package Zoo;

public class Bear extends Animal {
    // Create a static int that keeps track of the number of bears created.
    private static int numOfBears = 0;

    // Public static getter for the number of bears created.
    public static int getNumOfBears() {
        return numOfBears;
    }

    // Create a constructor.
    public Bear() {
        super();
        numOfBears++;
    }

    // Create a constructor that will have all the fields I want.
    public Bear(String sex, int age, int weight, String animalName,
                String animalID, String animalBirthDate, String animalColor,
                String animalOrigin, String animalArrivalDate) {
        super(sex, age, weight, animalName, animalID, animalBirthDate, animalColor, animalOrigin, animalArrivalDate);
        numOfBears++;
    }
}
