package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Schedule {
    private static ObservableList<Person> customers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public static void addCustomer(Customer cust) {
        customers.add(cust);
    }

    public static void addAppointment(Appointment appt) {
        appointments.add(appt);
    }

    public static ObservableList<Person> getCustomers() {
        return customers;
    }

    public static ObservableList<Appointment> getAppointments() {
        return appointments;
    }
}
