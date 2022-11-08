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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Customer;
import sample.model.Schedule;
import sample.utility.Country;
import sample.utility.DisplayLocations;
import sample.utility.Division;
import sample.utility.Location;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class CustomerUpdateController implements Initializable {
    @FXML private ComboBox<Location> countrySelector;
    @FXML private ComboBox<Location> divisionSelector;
    @FXML private TextField idField;
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

    public void setCurrentCustomer(Customer customer) {
        Stream<Division> divisionStream;
        Stream<Country> countryStream;
        Division customerDivision;
        Country customerCountry;

        idField.setText(Integer.toString(customer.getId()));
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        postalCodeField.setText(customer.getPostalCode());
        phoneField.setText(customer.getPhone());

        divisionStream = DisplayLocations.getDivisions().stream();
        countryStream = DisplayLocations.getCountries().stream();
        customerDivision = divisionStream.filter(div -> div.getId() == customer.getDivisionId()).toList().get(0);
        customerCountry = countryStream.filter(c -> c.getId() == customer.getCountryId()).toList().get(0);

        System.out.println("Current Customer Division:" + customerDivision.getName());

        countrySelector.setValue(customerCountry);
        filterDivision();
        divisionSelector.setValue(customerDivision);
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

    public PreparedStatement newCustomerQuery() throws SQLException {
        PreparedStatement ps;
        int divisionId;
        String name, address, postalCode, phone, query;

        name = nameField.getText();
        address = addressField.getText();
        postalCode = postalCodeField.getText();
        phone = phoneField.getText();
        divisionId = divisionSelector.getValue().getId();

        query = """
                UPDATE customers 
                SET customer_name = ?, address = ?, postal_code = ?, phone = ?,
                division_id = ?, last_update=CURRENT_TIMESTAMP, last_updated_by=?
                WHERE customer_id = ?
                """;

        ps = Query.pendingStatement(query);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionId);
        ps.setString(6, Schedule.getCurrentUser().getName());
        ps.setInt(7, Integer.parseInt(idField.getText()));

        return ps;
    }

    public void save(ActionEvent event) throws SQLException, IOException {
        DBConnector.connect();
        newCustomerQuery().executeUpdate();
        DBConnector.closeConnection();
        mainFormRedirect(event);
    }
}