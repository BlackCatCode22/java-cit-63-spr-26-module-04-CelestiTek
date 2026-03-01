package Zoo;

public class Tiger extends Animal {
    // Create a static int that keeps track of the number of tigers created.
    private static int numOfTigers = 0;

    // Public static getter for the number of tigers created.
    public static int getNumOfTigers() {
        return numOfTigers;
    }

    // Create a constructor.
    public Tiger() {
        super();
        numOfTigers++;
    }

    // Create a constructor that will have all the fields I want.
    public Tiger(String sex, int age, int weight, String animalName,
                 String animalID, String animalBirthDate, String animalColor,
                 String animalOrigin, String animalArrivalDate) {
        super(sex, age, weight, animalName, animalID, animalBirthDate, animalColor, animalOrigin, animalArrivalDate);
        numOfTigers++;
    }
}
