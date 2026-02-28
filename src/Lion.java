public class Lion extends Animal {

    // Unique features
    private String prideName;
    private double maneSize; // in inches or centimeters

    // Constructor
    public Lion(String name, int age, String species, String prideName, double maneSize) {
        super(name, age, species);
        this.prideName = prideName;
        this.maneSize = maneSize;
    }

    // Getters
    public String getPrideName() {
        return prideName;
    }

    public double getManeSize() {
        return maneSize;
    }

    // Setters
    public void setPrideName(String prideName) {
        this.prideName = prideName;
    }

    public void setManeSize(double maneSize) {
        this.maneSize = maneSize;
    }
}
