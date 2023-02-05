package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Customer;
import sample.utility.Notification;
import sample.utility.Session;
import sample.utility.SimpleSearch;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PosCtrl implements Initializable {
    @FXML TextField customerSearchField;
    @FXML Button customerSearchBtn, newOrderBtn;
    @FXML TableView<Customer> customerTable;
    @FXML TableColumn<Customer, String> customerNameCol;
    @FXML TableColumn<Customer, String> customerEmailCol;
    @FXML TableColumn<Customer, String> customerPhoneCol;
    @FXML ObservableList<Customer> customerContainer = FXCollections.observableArrayList();
    private ArrayList<Customer> scannedCustomers;

    private Parent root;
    private Scene scene;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int custId;
        String customersQuery, name, phone, email;
        ResultSet customerRs;

        scannedCustomers = new ArrayList<Customer>();
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        customersQuery = """
                SELECT * FROM customer; 
                """;

        // Scan for and get list of customers
        try {
            DBConnector.connect();
            Query.runQuery(customersQuery);
            customerRs = Query.getResults();

            while (customerRs.next()) {
                name = customerRs.getString("name");
                email = customerRs.getString("email");
                phone = customerRs.getString("phone");
                custId = customerRs.getInt("id");

                // scanned customers will contain all customers for performing search
                scannedCustomers.add(new Customer(custId, name, email, phone));

                // customer container will reflect search result
                customerContainer.add(new Customer(custId, name, email, phone));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        customerTable.setItems(customerContainer);
    }

    private void switchForms(ActionEvent e, String formName, String title) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/" + formName));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setTitle(title);
        scene = new Scene(root);
        stage.setScene(scene);
        root.requestFocus();
        stage.show();
    }

    public void searchForCustomer(ActionEvent e) {
        String searchQuery = customerSearchField.getText();
        customerContainer.clear();
        customerContainer.addAll(SimpleSearch.searchCustomer(scannedCustomers, searchQuery));
        customerTable.setItems(customerContainer);
    }

    public void endSession(ActionEvent event) throws IOException {
        switchForms(event, "log-in.fxml", "Capstone Music");
    }

    public void newOrder(ActionEvent event) throws IOException {
        SelectionModel<Customer> customerSelectionModel = customerTable.getSelectionModel();
        if (customerSelectionModel.isEmpty()) {
            Notification.noSelection("New Order", "customer");
            return;
        }

        // TODO: you may need to "flush" the current customer at the end of the order session
        Session.setCurrentCustomer(customerSelectionModel.getSelectedItem());
        switchForms(event, "new-order.fxml", "New Order");
    }

    public void addNewCustomer(ActionEvent event) throws IOException {
        switchForms(event, "new-customer-form.fxml", "New Customer");
    }
}
