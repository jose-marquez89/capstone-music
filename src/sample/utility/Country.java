package sample.utility;

/**
 * Models countries in the database to
 * allow filtering on several controllers.
 *
 * @author Jose Marquez
 */
public class Country extends Location {
    private int id;
    private String name;

    public Country(int id, String name) {
        super(id, name);
    }
}
