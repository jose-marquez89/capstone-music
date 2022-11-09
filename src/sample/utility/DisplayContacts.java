package sample.utility;

import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DisplayContacts {
    private static ArrayList<Contact> contacts = new ArrayList<>();

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

    public static ArrayList<Contact> getContacts() {
        return contacts;
    }
}
