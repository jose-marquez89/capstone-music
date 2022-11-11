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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Customer;
import sample.model.Schedule;
import sample.utility.*;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * Controls the form responsible for adding new customers.
 *
 * Allows the addition of new customers via a new form extending
 * out of the main user interface.
 *
 * @author Jose Marquez
 */
public class CustomerAddController implements Initializable {
    @FXML private ComboBox<Location> countrySelector;
    @FXML private ComboBox<Location> divisionSelector;
    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField postalCodeField;
    @FXML private TextField phoneField;

    private ObservableList<Division> divisionContainer = FXCollections.observableArrayList();
    private Parent root;
    private Scene scene;
    private Stage stage;

    public void setComboBoxDisplay(ComboBox<Location> cb) {
        cb.setCellFactory(cell -> new ListCell<Location>() {
            @Override
            protected void updateItem(Location l, boolean empty) {
                super.updateItem(l, empty);
                setText(empty ? null : l.getName());
            }
        });

        cb.setButtonCell(new ListCell<Location>() {
            @Override
            protected void updateItem(Location l, boolean empty) {
                super.updateItem(l, empty);
                setText((empty) ? null : l.getName());
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setComboBoxDisplay(countrySelector);
        setComboBoxDisplay(divisionSelector);
        countrySelector.getItems().addAll(DisplayLocations.getCountries());
    }

    public void filterDivision() {
        int countryId;
        if (countrySelector.getValue() != null) {
            countryId = countrySelector.getValue().getId();
            Stream<Division> divisionStream = DisplayLocations.getDivisions().stream();
            List<Division> filteredDivisions = divisionStream.filter(div -> div.getCountryId() == countryId).toList();
            divisionSelector.getItems().clear();
            divisionSelector.getItems().addAll(filteredDivisions);
        }
    }

    public void mainFormRedirect(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/user-dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setX(100.0);
        stage.setY(50.0);
        stage.show();
    }

    public PreparedStatement newCustomerQuery(String name, String address, String postalCode,
                                              String phone, Location division) throws SQLException {
        PreparedStatement ps;
        int divisionId;
        String query;

        divisionId = division.getId();

        query = """
                INSERT INTO customers (customer_name, address, postal_code, phone, division_id, created_by, last_updated_by)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        ps = Query.pendingStatement(query);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionId);
        ps.setString(6, Schedule.getCurrentUser().getName());
        ps.setString(7, Schedule.getCurrentUser().getName());

        return ps;
    }

    public void save(ActionEvent event) throws SQLException, IOException {
        String[] validationFields = new String[4];
        String name, address, postalCode, phone;
        Location division;

        name = nameField.getText();
        address = addressField.getText();
        postalCode = postalCodeField.getText();
        phone = phoneField.getText();
        division = divisionSelector.getValue();

        validationFields[0] = name;
        validationFields[1] = address;
        validationFields[2] = postalCode;
        validationFields[3] = phone;

        if (!CustomerValidator.validateTextFields(validationFields)) {
            Notification.customerFieldsInvalid();
            return;
        }

        if (!CustomerValidator.validateDivision(division)) {
            Notification.customerFieldsInvalid();
            return;
        }

        DBConnector.connect();
        newCustomerQuery(name, address, postalCode, phone, division).executeUpdate();
        DBConnector.closeConnection();
        mainFormRedirect(event);
    }
}
