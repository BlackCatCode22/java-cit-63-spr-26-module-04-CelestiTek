public class Hyena extends Animal {

    // Unique features
    private String clanName;
    private boolean laughsLoudly;

    // Constructor
    public Hyena(String name, int age, String species, String clanName, boolean laughsLoudly) {
        super(name, age, species);
        this.clanName = clanName;
        this.laughsLoudly = laughsLoudly;
    }

    // Getters
    public String getClanName() {
        return clanName;
    }

    public boolean isLaughsLoudly() {
        return laughsLoudly;
    }

    // Setters
    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public void setLaughsLoudly(boolean laughsLoudly) {
        this.laughsLoudly = laughsLoudly;
    }
}
