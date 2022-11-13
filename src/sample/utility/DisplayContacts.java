package sample.utility;

import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Provides a container for contact display in several
 * combo boxes in add/update forms.
 *
 * @author Jose Marquez
 */
public class DisplayContacts {
    private static ArrayList<Contact> contacts = new ArrayList<>();

    /**
     * Fills the <code>contacts</code> <code>ArrayList</code> with contacts
     * to display in appointment add/update forms.
     *
     * @throws SQLException
     */
    public static void initializeContacts() throws SQLException {
        ResultSet results;

        DBConnector.connect();
        Query.runQuery("SELECT * FROM contacts;");
        results = Query.getResults();

        while (results.next()) {
            Contact newContact = new Contact(results.getInt("contact_id"), results.getString("contact_name"));
            contacts.add(newContact);
        }

        DBConnector.closeConnection();
    }

    /**
     * Allows the utilization of the contacts list across the application.
     *
     * @return an <code>ArrayList</code> of <code>Contact</code> objects
     */
    public static ArrayList<Contact> getContacts() {
        return contacts;
    }
}
