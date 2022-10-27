package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Schedule {
    private static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private static User currentUser;

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

    public static void clearCustomers() {
        customers.clear();
    }

    public static ObservableList<Appointment> getAppointments() {
        return appointments;
    }
}
