package Animal;

public class Tiger extends Animal {

    // Unique features
    private String stripePattern;   // e.g., "dense", "sparse", "dark"
    private double territorySize;   // in square miles or km

    // Constructor
    public Tiger(String name, int age, String species, String stripePattern, double territorySize) {
        super(name, age, species);
        this.stripePattern = stripePattern;
        this.territorySize = territorySize;
    }

    // Getters
    public String getStripePattern() {
        return stripePattern;
    }

    public double getTerritorySize() {
        return territorySize;
    }

    // Setters
    public void setStripePattern(String stripePattern) {
        this.stripePattern = stripePattern;
    }

    public void setTerritorySize(double territorySize) {
        this.territorySize = territorySize;
    }
}
