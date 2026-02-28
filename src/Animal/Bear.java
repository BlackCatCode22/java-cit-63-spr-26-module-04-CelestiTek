package Animal;

public class Bear extends Animal {

    // Unique features
    private boolean isHibernating;
    private double clawLength; // in inches or centimeters

    // Constructor
    public Bear(String name, int age, String species, boolean isHibernating, double clawLength) {
        super(name, age, species);
        this.isHibernating = isHibernating;
        this.clawLength = clawLength;
    }

    // Getters
    public boolean isHibernating() {
        return isHibernating;
    }

    public double getClawLength() {
        return clawLength;
    }

    // Setters
    public void setHibernating(boolean hibernating) {
        this.isHibernating = hibernating;
    }

    public void setClawLength(double clawLength) {
        this.clawLength = clawLength;
    }
}
