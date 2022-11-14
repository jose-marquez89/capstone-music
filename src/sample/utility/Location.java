package sample.utility;

/**
 * Allows the creation of classes used to model
 * different types of locations such as Country and Division.
 *
 * @author Jose Marquez
 */
abstract public class Location {
    private int id;
    private String name;

    /**
     * The constructor for the <code>Location</code> class
     *
     * @param id an integer id for the intended location
     * @param name a string name for the intended location
     */
    public Location(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
