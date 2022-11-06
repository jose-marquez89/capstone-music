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
import sample.model.Customer;
import sample.model.Schedule;

import javafx.event.ActionEvent;
import sample.utility.Notification;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Stream;

public class UserDashboardController implements Initializable {
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
    @FXML private Label logInAlertText;
    @FXML private ImageView logInAlertImage;
    private ObservableList<Appointment> appointmentsContainer = FXCollections.observableArrayList();
    private String appointmentMessage;
    private Parent root;
    private Scene scene;
    private Stage stage;


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

        appointmentsContainer.addAll(Schedule.getAppointments());
        customerTable.setItems(Schedule.getCustomers());
        appointmentTable.setItems(appointmentsContainer);

        if (ChronoUnit.MINUTES.between(logInDateTime.toLocalTime(), LocalDateTime.now()) > -15) {
            nearbyApptsStream = Schedule.getAppointments().stream();
            durations = nearbyApptsStream
                    .filter(a ->
                            ChronoUnit.MINUTES.between(logInDateTime, a.getStart()) <= 15
                            && ChronoUnit.MINUTES.between(logInDateTime, a.getStart()) >= 0)
                    .sorted((a, b) -> a.getStart().compareTo(b.getStart()))
                    .toList();

            Schedule.getAppointments().stream().forEach(a -> System.out.println("Start" + a.getStart().toString()));


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

    public void populateCustomers() throws SQLException {
        int id;
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
        String title, location, description, contact, type;
        LocalDateTime start, end;
        ResultSet apptQueryResult;
        // TODO: remove user specific query elements if necessary
        // String queryUserTail = Schedule.getCurrentUser().getId() + ";";
        String getApptsQuery = """
                SELECT *
                FROM appointments AS a
                JOIN contacts AS c
                ON a.contact_id = c.contact_id;""";
                // --WHERE a.user_id = """ + queryUserTail;


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

    public void seeAllAppointments() {
        appointmentsContainer.clear();
        appointmentsContainer.addAll(Schedule.getAppointments());
    }

    public void seeWeeklyAppointments() {
        Stream<Appointment> apptsStream = Schedule.getAppointments().stream();
        int currentWeek = LocalDateTime.now().get(WeekFields.of(Locale.getDefault()).weekOfYear());
        appointmentsContainer.clear();
        apptsStream
                .filter(a -> a.getStart().get(WeekFields.of(Locale.getDefault()).weekOfYear()) == currentWeek)
                .forEach(a -> appointmentsContainer.add(a));
    }

    public void seeMonthlyAppointments() {
        Stream<Appointment> apptsStream = Schedule.getAppointments().stream();
        int currentMonth = LocalDateTime.now().getMonthValue();
        appointmentsContainer.clear();
        apptsStream
                .filter(a -> a.getStart().getMonthValue() == currentMonth)
                .forEach(a -> appointmentsContainer.add(a));
    }

    public void updateAppointment(ActionEvent event) throws IOException {
        AppointmentUpdateController updateController;
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        FXMLLoader productFormLoader = new FXMLLoader(getClass().getResource("../view/appointment-update-form.fxml"));

        if (selectedAppointment == null) {
            Notification.noSelection("Update/Delete Appointment", "appointment");
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
}
