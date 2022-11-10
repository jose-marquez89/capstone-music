package sample.model;

/**
 * A class used for company contacts in the application.
 *
 * @author Jose Marquez
 */
public class Contact extends Person {
    private int id;
    private String name;
    public Contact(int id, String name) {
        super(id, name);
    }
}
