package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Schedule {
    private static ObservableList<Person> customers = FXCollections.observableArrayList();
    private static ObservableList<Person> appointments = FXCollections.observableArrayList();

    public static void addCustomer(Customer cust) {
        customers.add(cust);
    }

    // TODO: other methods
}
