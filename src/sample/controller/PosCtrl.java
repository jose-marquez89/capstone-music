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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PosCtrl implements Initializable {
    @FXML TableView<Customer> customerTable;
    @FXML TableColumn<Customer, String> customerNameCol;
    @FXML TableColumn<Customer, String> customerEmailCol;
    @FXML TableColumn<Customer, String> customerPhoneCol;
    @FXML ObservableList<Customer> customerContainer = FXCollections.observableArrayList();

    private Parent root;
    private Scene scene;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int custId;
        String customersQuery, name, phone, email;
        ResultSet customerRs;

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
                customerContainer.add(new Customer(custId, name, email, phone));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        customerTable.setItems(customerContainer);
    }

    public void endSession(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/log-in.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Capstone Music");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
