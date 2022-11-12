package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Appointment;
import sample.model.Contact;
import sample.model.Customer;
import sample.model.Schedule;

import javafx.event.ActionEvent;
import sample.utility.*;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Stream;

/**
 * Controls the main user interface form.
 *
 * Provides a controller for the main user interface as well
 * as the additional reports and schedule.
 *
 * @author Jose Marquez
 */
public class UserDashboardController implements Initializable {
    @FXML private ComboBox<Contact> contactSelector;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Customer, Integer> customerIdCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerAddressCol;
    @FXML private TableColumn<Customer, String> postalCodeCol;
    @FXML private TableColumn<Customer, String> phoneNumberCol;
    @FXML private TableColumn<Customer, String> customerDivisionCol;
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
    @FXML private TableView<Appointment> scheduleTable;
    @FXML private TableView<MonthTypeRecord> apptsByTypeMonth;
    @FXML private TableView<CountryRecord> customersByCountry;
    @FXML private TableColumn<Appointment, Integer> scheduleApptIdCol;
    @FXML private TableColumn<Appointment, String> scheduleTitleCol;
    @FXML private TableColumn<Appointment, String> scheduleTypeCol;
    @FXML private TableColumn<Appointment, String> scheduleDescriptionCol;
    @FXML private TableColumn<Appointment, LocalDateTime> scheduleStartCol;
    @FXML private TableColumn<Appointment, LocalDateTime> scheduleEndCol;
    @FXML private TableColumn<Appointment, Integer> scheduleCustomerIdCol;
    @FXML private TableColumn<ReportRecord, String> byTypeNameCol;
    @FXML private TableColumn<ReportRecord, String> byMonthNameCol;
    @FXML private TableColumn<ReportRecord, Integer> byMonthAmountCol;
    @FXML private TableColumn<ReportRecord, String> byCountryNameCol;
    @FXML private TableColumn<ReportRecord, Integer> byCountryAmountCol;
    @FXML private Label logInAlertText;
    @FXML private ImageView logInAlertImage;
    @FXML private ObservableList<Appointment> appointmentsContainer = FXCollections.observableArrayList();
    @FXML private ObservableList<Appointment> scheduleContainer = FXCollections.observableArrayList();
    @FXML private ObservableList<MonthTypeRecord> monthTypeReportContainer = FXCollections.observableArrayList();
    @FXML private ObservableList<CountryRecord> countryReportContainer = FXCollections.observableArrayList();
    private String appointmentMessage;
    private Parent root;
    private Scene scene;
    private Stage stage;

    /**
     * Sets table views and their respective columns for the main user
     * interface.
     *
     * <h4>Lambda Expression</h4>
     * The <code>contactSelector</code> combo box requires
     * a lambda expression to take the incoming cell and
     * override the <code>updateItem</code> method. This
     * allows the display of the contact's name while also
     * enabling the identification of the contact via their id.
     *
     * @param url the URL object passed in from the background
     * @param rb the resource bundle passed in from the background
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // get all initially available customers
        int nearbyMeetingId;
        String meetingDate, meetingTime;
        String currentUserId = Integer.toString(Schedule.getCurrentUser().getId());
        LocalDateTime logInDateTime = Schedule.getCurrentUser().getLogInDateTime();
        Stream<Appointment> nearbyApptsStream;
        List<Appointment> durations;
        // customer table columns
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));

        // appointment table columns
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptUserIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        apptCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        // schedule table columns
        scheduleApptIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        scheduleCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        scheduleTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        scheduleDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        scheduleTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        scheduleStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        scheduleEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));


        // by month report columns
        byMonthNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        byTypeNameCol.setCellValueFactory(new PropertyValueFactory<>("subName"));
        byMonthAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // by country customer report columns
        byCountryNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        byCountryAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        formatDateCol(apptStartCol);
        formatDateCol(apptEndCol);
        formatDateCol(scheduleStartCol);
        formatDateCol(scheduleEndCol);

        try {
            populateCustomers();
            populateAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        contactSelector.getItems().addAll(DisplayContacts.getContacts());
        appointmentsContainer.addAll(Schedule.getAppointments());
        scheduleContainer.addAll(Schedule.getAppointments());
        customerTable.setItems(Schedule.getCustomers());
        appointmentTable.setItems(appointmentsContainer);
        scheduleTable.setItems(scheduleContainer);
        apptsByTypeMonth.setItems(monthTypeReportContainer);
        customersByCountry.setItems(countryReportContainer);


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


        if (ChronoUnit.MINUTES.between(logInDateTime.toLocalTime(), LocalDateTime.now()) > -15) {
            nearbyApptsStream = Schedule.getAppointments().stream();
            durations = nearbyApptsStream
                    .filter(a ->
                            ChronoUnit.MINUTES.between(logInDateTime, a.getStart()) <= 15
                            && ChronoUnit.MINUTES.between(logInDateTime, a.getStart()) >= 0)
                    .sorted((a, b) -> a.getStart().compareTo(b.getStart()))
                    .toList();

            if (durations.isEmpty()) {
                logInAlertImage.setImage(new Image(getClass().getResourceAsStream("../view/green-clock-smaller.png")));
                logInAlertText.setText("There are no upcoming appointments within 15 minutes");
            } else {
                nearbyMeetingId = durations.get(0).getId();
                meetingDate = durations.get(0).getStart().format(DateTimeFormatter.ofPattern("MM/d/yyyy"));
                meetingTime = durations.get(0).getStart().format(DateTimeFormatter.ofPattern("h:mm a"));
                appointmentMessage = String.format(
                        "Appointment ID %d on %s at %s begins within 15 minutes",
                        nearbyMeetingId, meetingDate, meetingTime);
                System.out.println("Appointments within 15");
                durations.forEach(a -> System.out.println(a.getStart().toString()));
                logInAlertImage.setImage(new Image(getClass().getResourceAsStream("../view/red-clock-smaller.png")));
                logInAlertText.setText(appointmentMessage);
            }
        } else {
            logInAlertImage.setVisible(false);
            logInAlertText.setVisible(false);
        }
    }

    /**
     * Allows the formatting of the date column in the appointments
     * table to display in standard date and 12-hour time format.
     *
     * <h4>Lambda Expression</h4>
     * A lambda expression is used in the column's <code>setCellFactory</code>
     * method. This allows the incoming cell to be formatted from standard
     * <code>LocalDateTime</code> format to a more user-friendly 12-hour format.
     * (Example: 12/3/2022 6:06 PM)
     *
     * @param col the <code>LocalDateTime</code> column for which to set the
     *            cell factory
     */
    public void formatDateCol(TableColumn col) {
        col.setCellFactory(cell -> new TableCell<Appointment, LocalDateTime>() {
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
    }

    /**
     * Populates the body of customer in static resources within <code>Schedule</code>.
     * <p>
     * Reads the MySQL database for customer records and creates a
     * <code>Customer</code> object for each available customer in
     * the database to be displayed in the user interface.
     *
     * @throws SQLException
     */
    public void populateCustomers() throws SQLException {
        int id, divisionId, countryId;
        LocalDateTime createDate, lastUpdate;
        String name, address, pc, phone, div, country, createdBy, lastUpdatedBy;
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
            divisionId = customerQueryResult.getInt("division_id");
            countryId = customerQueryResult.getInt("country_id");
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

           Customer newCustomer = new Customer(id, divisionId, countryId, name,
                   createDate, createdBy, lastUpdate, lastUpdatedBy, address, pc, phone, div, country);

           Schedule.addCustomer(newCustomer);
        }

        DBConnector.closeConnection();
    }

    /**
     * Populates the body of appointments in static resources within <code>Schedule</code>.
     *
     * Reads the MySQL database for appointment records and creates an
     * <code>Appointment</code> object for each available appointment in
     * the database to be displayed in the user interface.
     *
     * @throws SQLException
     */
    public void populateAppointments() throws SQLException {
        int id, customerId, userId, contactId;
        String title, location, description, contact, type;
        LocalDateTime start, end;
        ResultSet apptQueryResult;
        String getApptsQuery = """
                SELECT *
                FROM appointments AS a
                JOIN contacts AS c
                ON a.contact_id = c.contact_id;""";

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

    /**
     * Allows the user to see all existing appointments in the
     * user interface.
     *
     * Removes all filters from the appointments table that may
     * have been set by the user via radio button selection.
     */
    public void seeAllAppointments() {
        appointmentsContainer.clear();
        appointmentsContainer.addAll(Schedule.getAppointments());
    }

    /**
     * Filters appointments by the current week.
     *
     * <h4>Lambda Expression</h4>
     * This method uses two lambda expressions:
     * <ul>
     *     <li>a <code>Stream</code> of type <code>Appointment</code> is filtered to match those that are in the current week</li>
     *     <li>each <code>Appointment</code> object in the stream is added to the <code>appoinmentsContainer</code> to effectively filter display</li>
     * </ul>
     */
    public void seeWeeklyAppointments() {
        Stream<Appointment> apptsStream = Schedule.getAppointments().stream();
        int currentWeek = LocalDateTime.now().get(WeekFields.of(Locale.getDefault()).weekOfYear());
        appointmentsContainer.clear();
        apptsStream
                .filter(a -> a.getStart().get(WeekFields.of(Locale.getDefault()).weekOfYear()) == currentWeek)
                .forEach(a -> appointmentsContainer.add(a));
    }

    /**
     * Filters appointments by the current month.
     *
     * <h4>Lambda Expression</h4>
     * This method uses two lambda expressions:
     * <ul>
     *     <li>a <code>Stream</code> of type <code>Appointment</code> is filtered to match those that are in the current week</li>
     *     <li>each <code>Appointment</code> object in the stream is added to the <code>appointmentsContainer</code> to effectively filter display</li>
     * </ul>
     */
    public void seeMonthlyAppointments() {
        Stream<Appointment> apptsStream = Schedule.getAppointments().stream();
        int currentMonth = LocalDateTime.now().getMonthValue();
        appointmentsContainer.clear();
        apptsStream
                .filter(a -> a.getStart().getMonthValue() == currentMonth)
                .forEach(a -> appointmentsContainer.add(a));
    }

    /**
     * Filters the appointments for a selected contact in the "Reports" tab.
     *
     * <h4>Lambda Expression</h4>
     * <ul>
     *     <li>a <code>Stream</code> of type <code>Appointment</code> is filtered to match those associated with the selected contact</li>
     *     <li>each <code>Appointment</code> object in the stream is added to the <code>scheduleContainer</code> to effectively filter the schedule display</li>
     * </ul>
     * @param event the event triggered by user interaction with interface components
     */
    public void seeContactSchedule(ActionEvent event) {
        Contact selectedContact = contactSelector.getValue();
        Stream<Appointment> apptsStream = Schedule.getAppointments().stream();
        scheduleContainer.clear();
        apptsStream
                .filter(a -> a.getContactId() == selectedContact.getId())
                .forEach(a -> scheduleContainer.add(a));
    }

    /**
     * Triggers the initialization of the appointment update form.
     *
     * The method loads the currently selected appointment into the
     * impending appointment update form. If there is no selection,
     * the method will trigger a notification.
     *
     * @param event the event triggered via user interaction with interface components
     * @throws IOException
     * @see Notification#noSelection(String, String)
     */
    public void updateAppointment(ActionEvent event) throws IOException {
        AppointmentUpdateController updateController;
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        FXMLLoader appointmentFormLoader = new FXMLLoader(getClass().getResource("../view/appointment-update-form.fxml"));

        if (selectedAppointment == null) {
            Notification.noSelection("Update/Delete Appointment", "appointment");
        } else {
            root = appointmentFormLoader.load();
            updateController = appointmentFormLoader.getController();
            updateController.setCurrentAppointment(selectedAppointment);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    // TODO: add more javadocs 2022-11-11
    public void addAppointment(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/appointment-add-form.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void deleteAppointment(ActionEvent event) throws IOException, SQLException {
        Optional<ButtonType> result;
        Appointment userSelection = appointmentTable.getSelectionModel().getSelectedItem();

        if (userSelection == null) {
            Notification.noSelection("Update/Delete Appointment", "appointment");
            return;
        }

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
            appointmentsContainer.clear();
            appointmentsContainer.addAll(Schedule.getAppointments());
        }
    }

    public void addCustomer(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/customer-add-form.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void updateCustomer(ActionEvent event) throws IOException {
        CustomerUpdateController updateController;
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        FXMLLoader customerFormLoader = new FXMLLoader(getClass().getResource("../view/customer-update-form.fxml"));

        if (selectedCustomer == null) {
            Notification.noSelection("Update/Delete Customer", "customer");
        } else {
            root = customerFormLoader.load();
            updateController = customerFormLoader.getController();
            updateController.setCurrentCustomer(selectedCustomer);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void deleteCustomer() throws SQLException {
        Optional<ButtonType> result;
        List<Appointment> appts;
        Stream<Appointment> appointmentStream = Schedule.getAppointments().stream();
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        Alert confirmDelete = new Alert(AlertType.CONFIRMATION);


        if (selectedCustomer == null) {
            Notification.noSelection("Update/Delete Customer", "customer");
            return;
        }

        confirmDelete.setTitle("Update/Delete Customer");
        confirmDelete.setContentText(String.format(
                "Are you sure you want to delete the customer %s?", selectedCustomer.getName()));

        String deleteCustomerQuery = String.format("""
                DELETE FROM customers
                WHERE customer_id = %d;
                """, selectedCustomer.getId());

        appts = appointmentStream.filter(a -> a.getCustomerId() == selectedCustomer.getId()).toList();

        if (appts.size() > 0) {
            Notification.customerHasAppointments();
        } else {
            result = confirmDelete.showAndWait();
            if (result.get() == ButtonType.OK) {
                DBConnector.connect();
                Query.runUpdate(deleteCustomerQuery);
                DBConnector.closeConnection();
                Notification.customerDeleteConfirmation(selectedCustomer.getName());
                populateCustomers();
            }
        }
    }

    public void populateMonthTypeTable(ObservableList<MonthTypeRecord> reportContainer, String query) throws SQLException {
        String name, subName;
        int amount;
        ResultSet results;
        DBConnector.connect();
        Query.runQuery(query);
        results = Query.getResults();

        reportContainer.clear();

        while (results.next()) {
            name = results.getString("name");
            subName = results.getString("sub_name");
            amount = results.getInt("amount");
            MonthTypeRecord record = new MonthTypeRecord(name, subName, amount);
            reportContainer.add(record);
        }

        DBConnector.closeConnection();
    }

    public void populateCountryTable(ObservableList<CountryRecord> reportContainer, String query) throws SQLException {
        String name;
        int amount;
        ResultSet results;
        DBConnector.connect();
        Query.runQuery(query);
        results = Query.getResults();

        reportContainer.clear();

        while (results.next()) {
            name = results.getString("name");
            amount = results.getInt("amount");
            CountryRecord record = new CountryRecord(name, amount);
            reportContainer.add(record);
        }

        DBConnector.closeConnection();
    }

    public void refreshReports() throws SQLException {
        String monthTypeQuery, customersQuery;
        Contact defaultContact = contactSelector.getItems().get(0);
        contactSelector.setValue(defaultContact);
        seeContactSchedule(new ActionEvent());

        monthTypeQuery = """
                WITH raw AS (
                    SELECT
                        MONTH(start) AS month_num,
                        MONTHNAME(start) AS name,
                        type AS sub_name,
                        COUNT(appointment_id) AS amount
                    FROM appointments
                    GROUP BY MONTH(start), MONTHNAME(start), type
                    ORDER BY MONTH(start), COUNT(appointment_id) DESC
                )
                
                SELECT name, sub_name, amount
                FROM raw;
                """;

        customersQuery = """
                WITH base AS (
                	SELECT cy.country, cs.customer_id
                	FROM customers AS cs
                	JOIN first_level_divisions AS d ON cs.division_id = d.division_id
                	JOIN countries AS cy ON d.country_id = cy.country_id
                ),
                                
                raw AS (
                	SELECT
                		country AS name,
                		COUNT(customer_id) AS amount
                	FROM base
                	GROUP BY name
                )
                                
                SELECT name, amount
                FROM raw;
                """;

        populateMonthTypeTable(monthTypeReportContainer, monthTypeQuery);
        populateCountryTable(countryReportContainer, customersQuery);
    }
}
