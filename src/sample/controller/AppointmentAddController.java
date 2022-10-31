package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Contact;
import sample.model.Schedule;
import sample.utility.DisplayMinutes;
import sample.utility.Minute;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class AppointmentAddController implements Initializable {
    @FXML private TextField titleField;
    @FXML private TextField locationField;
    @FXML private TextField descriptionField;
    @FXML private TextField typeField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<Integer> startHourSelector;
    @FXML private ComboBox<Minute> startMinuteSelector;
    @FXML private ComboBox<String> startPeriodSelector;
    @FXML private ComboBox<Integer> endHourSelector;
    @FXML private ComboBox<Minute> endMinuteSelector;
    @FXML private ComboBox<String> endPeriodSelector;
    @FXML private ComboBox<Integer> customerIdSelector;
    @FXML private ComboBox<Integer> userIdSelector;
    @FXML private ComboBox<Contact> contactSelector;

    private Parent root;
    private Scene scene;
    private Stage stage;

    // TODO: implement save method

    private void setMinuteDisplay(ComboBox box) {
        box.setCellFactory(cell -> new ListCell<Minute>() {
            @Override
            protected void updateItem(Minute m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty ? null : m.getTextMinute());
            }
        });

        box.setButtonCell(new ListCell<Minute>() {
            @Override
            protected void updateItem(Minute m, boolean empty) {
                super.updateItem(m, empty);
                setText(empty ? null : m.getTextMinute());
            }
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ResultSet results;
        Integer[] hours = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] periods = {"AM", "PM"};

        try {
            // populate customer ids
            DBConnector.connect();
            Query.runQuery("SELECT customer_id FROM customers;");
            results = Query.getResults();

            while (results.next()) {
                customerIdSelector.getItems().add(results.getInt("customer_id"));
            }

            // populate user ids
            Query.runQuery("SELECT user_id FROM users;");
            results = Query.getResults();

            while (results.next()) {
                userIdSelector.getItems().add(results.getInt("user_id"));
            }

            // populate contact names
            Query.runQuery("SELECT * FROM contacts;");
            results = Query.getResults();

            while (results.next()) {
                Contact newContact = new Contact(results.getInt("contact_id"), results.getString("contact_name"));
                contactSelector.getItems().add(newContact);
            }

            DBConnector.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        startHourSelector.getItems().addAll(hours);
        startMinuteSelector.getItems().addAll(DisplayMinutes.getMinutes());
        startPeriodSelector.getItems().addAll(periods);
        endHourSelector.getItems().addAll(hours);
        endMinuteSelector.getItems().addAll(DisplayMinutes.getMinutes());
        endPeriodSelector.getItems().addAll(periods);

        setMinuteDisplay(startMinuteSelector);
        setMinuteDisplay(endMinuteSelector);

        contactSelector.setCellFactory(cell -> new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty ? null : c.getName());
            }
        });

        contactSelector.setButtonCell(new ListCell<Contact>() {
            @Override
            protected void updateItem(Contact c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty ? null : c.getName());
            }
        });
    }
    public void cancel(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/user-dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setX(100.0);
        stage.setY(50.0);
        stage.show();
    }

    public PreparedStatement newAppointmentQuery(Timestamp start, Timestamp end) throws SQLException {
        PreparedStatement ps;
        String query;
        String title = titleField.getText();
        String location = locationField.getText();
        String description = descriptionField.getText();
        String type = typeField.getText();
        int customerId = customerIdSelector.getSelectionModel().getSelectedItem();
        int userId = customerIdSelector.getSelectionModel().getSelectedItem();
        int contactId = contactSelector.getSelectionModel().getSelectedItem().getId();

        query = """
                INSERT INTO appointments (title, description, location, type, start, end, created_by, last_updated_by, customer_id, user_id, contact_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
        ps = Query.pendingStatement(query);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setString(7, Schedule.getCurrentUser().getName());
        ps.setString(8, Schedule.getCurrentUser().getName());
        ps.setInt(9, customerId);
        ps.setInt(10, userId);
        ps.setInt(11, contactId);

        return ps;
    }

    public void save() {
        // TODO: validate appointment time params
    }

}
