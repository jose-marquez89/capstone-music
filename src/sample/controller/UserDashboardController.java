package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Appointment;
import sample.model.Customer;
import sample.model.Schedule;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserDashboardController implements Initializable {
    private enum ViewMode {
        ALL,
        WEEKLY,
        MONTHLY
    }
    @FXML private TableView<Customer> customerTable;
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Customer, Integer> customerIdCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerAddressCol;
    @FXML private TableColumn<Customer, String> postalCodeCol;
    @FXML private TableColumn<Customer, String> phoneNumberCol;
    @FXML private TableColumn<Customer, String> customerDivisionCol;
    @FXML private TableColumn<Customer, String> customerDivCountryCol;
    @FXML private TableColumn<Appointment, Integer> apptIdCol;
    @FXML private TableColumn<Appointment, Integer> apptCustomerIdCol;
    @FXML private TableColumn<Appointment, Integer> apptUserIdCol;
    @FXML private TableColumn<Appointment, String> apptTitleCol;
    @FXML private TableColumn<Appointment, String> apptDescriptionCol;
    @FXML private TableColumn<Appointment, String> apptLocationCol;
    @FXML private TableColumn<Appointment, String> apptContactCol;
    @FXML private TableColumn<Appointment, String> apptTypeCol;
    @FXML private TableColumn<Appointment, LocalDateTime> apptStartCol;
    @FXML private TableColumn<Appointment, LocalDateTime> apptEndCol;
    private Parent root;
    private Scene scene;
    private Stage stage;
    private ViewMode appointmentsViewMode;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // get all initially available customers
        String currentUserId = Integer.toString(Schedule.getCurrentUser().getId());
        appointmentsViewMode = ViewMode.ALL;

        // customer table columns
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerDivCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));

        // appointment table columns
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        apptCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        // TODO: override updateItem to format date/time display
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        apptStartCol.setCellFactory(cell -> new TableCell<Appointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime dt, boolean empty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
                super.updateItem(dt, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(dt));
                }
            }
        });

        apptEndCol.setCellFactory(cell -> new TableCell<Appointment, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime dt, boolean empty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");
                super.updateItem(dt, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(dt));
                }
            }
        });

        try {
            populateCustomers();
            populateAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        customerTable.setItems(Schedule.getCustomers());
        appointmentTable.setItems(Schedule.getAppointments());
    }

    public void populateCustomers() throws SQLException {
        int id;
        LocalDateTime createDate, lastUpdate;
        String name, address, pc, phone, divId, div, country, createdBy, lastUpdatedBy;
        ResultSet customerQueryResult;
        String getCustomersQry = """
                SELECT *
                FROM customers AS c
                LEFT JOIN first_level_divisions AS d
                ON c.division_id = d.division_id
                LEFT JOIN  countries AS ct
                ON d.country_id = ct.country_id; 
        """;

        DBConnector.connect();
        Query.runQuery(getCustomersQry);
        customerQueryResult = Query.getResults();

        Schedule.clearCustomers();

        while (customerQueryResult.next()) {
            id = customerQueryResult.getInt("customer_id");
            name = customerQueryResult.getString("customer_name");
            createdBy = customerQueryResult.getString("created_by");
            lastUpdatedBy = customerQueryResult.getString("last_updated_by");
            address = customerQueryResult.getString("address");
            pc = customerQueryResult.getString("postal_code");
            phone = customerQueryResult.getString("phone");
            div = customerQueryResult.getString("division");
            country = customerQueryResult.getString("country");
            createDate = customerQueryResult
                    .getTimestamp("create_date")
                    .toLocalDateTime();
            lastUpdate = customerQueryResult
                    .getTimestamp("last_update")
                    .toLocalDateTime();

           Customer newCustomer = new Customer(id, name, createDate, createdBy, lastUpdate,
                   lastUpdatedBy, address, pc, phone, div, country);

           Schedule.addCustomer(newCustomer);
        }

        DBConnector.closeConnection();
    }

    public void populateAppointments() throws SQLException {
        int id, customerId, userId, contactId;
        String title, location, description, contact, type, getApptsQuery;
        LocalDateTime start, end;
        ResultSet apptQueryResult;
        // TODO: remove user specific query elements if necessary
        // String queryUserTail = Schedule.getCurrentUser().getId() + ";";
        String allApptsQuery = """
                SELECT *
                FROM appointments AS a
                JOIN contacts AS c
                ON a.contact_id = c.contact_id;""";
                // --WHERE a.user_id = """ + queryUserTail;
        String weeklyApptsQuery = """
                SELECT *
                FROM appointments AS a
                JOIN contacts AS c
                ON a.contact_id = c.contact_id
                WHERE WEEK(a.start) = WEEK(CURRENT_DATE());""";
        String monthlyApptsQuery = """
                SELECT *
                FROM appointments AS a
                JOIN contacts AS c
                ON a.contact_id = c.contact_id
                WHERE MONTH(a.start) = MONTH(CURRENT_DATE());""";

        // define query to run based on controller mode
        if (appointmentsViewMode == ViewMode.ALL) {
            getApptsQuery = allApptsQuery;
        } else if (appointmentsViewMode == ViewMode.WEEKLY) {
           getApptsQuery = weeklyApptsQuery;
        } else {
            getApptsQuery = monthlyApptsQuery;
        }

        DBConnector.connect();
        Query.runQuery(getApptsQuery);
        apptQueryResult = Query.getResults();

        Schedule.clearAppointments();

        while (apptQueryResult.next()) {
            id = apptQueryResult.getInt("appointment_id");
            customerId = apptQueryResult.getInt("customer_id");
            contactId = apptQueryResult.getInt("contact_id");
            userId = apptQueryResult.getInt("user_id");
            title = apptQueryResult.getString("title");
            description = apptQueryResult.getString("description");
            location = apptQueryResult.getString("location");
            contact = apptQueryResult.getString("contact_name");
            type = apptQueryResult.getString("type");
            start = apptQueryResult
                    .getTimestamp("start")
                    .toLocalDateTime();
            end = apptQueryResult
                    .getTimestamp("end")
                    .toLocalDateTime();

            Appointment newAppt = new Appointment(id, title, description, location,
                    contact, type, start, end, customerId, userId, contactId);

            Schedule.addAppointment(newAppt);
        }

        DBConnector.closeConnection();
    }

    public void seeAllAppointments(ActionEvent event) throws SQLException {
        appointmentsViewMode = ViewMode.ALL;
        populateAppointments();
    }

    public void seeWeeklyAppointments(ActionEvent event) throws SQLException {
        appointmentsViewMode = ViewMode.WEEKLY;
        populateAppointments();
    }

    public void seeMonthlyAppointments(ActionEvent event) throws SQLException {
        appointmentsViewMode = ViewMode.MONTHLY;
        populateAppointments();
    }

    public void updateAppointment(ActionEvent event) throws IOException {
        AppointmentUpdateController updateController;
        Alert noSelectionAlert = new Alert(AlertType.INFORMATION);
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        FXMLLoader productFormLoader = new FXMLLoader(getClass().getResource("../view/appointment-update-form.fxml"));

        // TODO: convert to static method
        noSelectionAlert.setTitle("Add/Update Appointment");
        noSelectionAlert.setContentText("You must select an appointment first.");

        if (selectedAppointment == null) {
            noSelectionAlert.show();
        } else {
            root = productFormLoader.load();
            updateController = productFormLoader.getController();
            updateController.setCurrentAppointment(selectedAppointment);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void addAppointment(ActionEvent event) throws IOException {
        // TODO: start as add form and switch to appt mod form
        root = FXMLLoader.load(getClass().getResource("../view/appointment-add-form.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteAppointment(ActionEvent event) throws IOException, SQLException {
        Optional<ButtonType> result;
        Appointment userSelection = appointmentTable.getSelectionModel().getSelectedItem();
        int apptId = userSelection.getId();
        String apptType = userSelection.getType();
        String deleteQuery = String.format("""
                DELETE FROM appointments
                WHERE appointment_id = %s;
                """, userSelection.getId());
        String areYouSureMessage = String.format("Are you sure you want to cancel this appointment?\nID: %d\nType: %s", apptId, apptType);
        String confirmationMessage = String.format("Appointment ID %d of type '%s' has been cancelled.", apptId, apptType);
        Alert confirmAction = new Alert(AlertType.CONFIRMATION);
        Alert deleteConfirmation = new Alert(AlertType.INFORMATION);

        confirmAction.setTitle("Delete Appointment");
        confirmAction.setContentText(areYouSureMessage);
        deleteConfirmation.setTitle("Delete Appointment");
        deleteConfirmation.setContentText(confirmationMessage);
        result = confirmAction.showAndWait();

        if (result.get() == ButtonType.OK) {
            DBConnector.connect();
            Query.runUpdate(deleteQuery);
            DBConnector.closeConnection();
            deleteConfirmation.show();
            populateAppointments();
        }
    }

    /*
    TODO: add fields to show for customers
    - customer_id
    - customer_name
    - address
    - postal_code
    - phone
    - division_id
     */

}
