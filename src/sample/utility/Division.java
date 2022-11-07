package sample.utility;

public class Division extends Location {
    private int id;
    private int countryId;
    private String name;

    public Division(int id, int countryId, String name) {
        super(id, name);
        this.countryId = countryId;
    }

    public int getCountryId() {
        return countryId;
    }
}
