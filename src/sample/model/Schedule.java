package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Models the schedule contained in the MySQL database.
 *
 * Provides static fields and methods used to access schedule
 * elements across the application.
 *
 * @author Jose Marquez
 */
public class Schedule {
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static ArrayList<Appointment> appointments = new ArrayList<Appointment>();
    private static User currentUser;

    /**
     * Sets the current logged in user for the entire application.
     *
     * @param user the <code>User</code> object representing the currently logged-in user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }
    public static void addCustomer(Customer cust) {
        customers.add(cust);
    }

    public static void addAppointment(Appointment appt) {
        appointments.add(appt);
    }

    public static ObservableList<Customer> getCustomers() {
        return customers;
    }

    /**
     * Resets the content of the customers <code>ObservableList</code>.
     */
    public static void clearCustomers() {
        customers.clear();
    }

    /**
     * Resets the content of the appointments <code>ObservableList</code>.
     */
    public static void clearAppointments() {
        appointments.clear();
    }

    public static ArrayList<Appointment> getAppointments() {
        return appointments;
    }
}
