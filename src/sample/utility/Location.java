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
