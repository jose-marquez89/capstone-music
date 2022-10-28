package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Appointment;
import sample.model.Customer;
import sample.model.Schedule;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

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


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // get all initially available customers
        String currentUserId = Integer.toString(Schedule.getCurrentUser().getId());

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

        try {
            updateCustomers();
            updateAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        customerTable.setItems(Schedule.getCustomers());
        appointmentTable.setItems(Schedule.getAppointments());
    }

    public void updateCustomers() throws SQLException {
        int id;
        ZonedDateTime createDate, lastUpdate;
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
                    .toLocalDateTime()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault());
            lastUpdate = customerQueryResult
                    .getTimestamp("last_update")
                    .toLocalDateTime()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault());

           Customer newCustomer = new Customer(id, name, createDate, createdBy, lastUpdate,
                   lastUpdatedBy, address, pc, phone, div, country);

           Schedule.addCustomer(newCustomer);
        }

        DBConnector.closeConnection();
    }

    public void updateAppointments() throws SQLException {
        int id, customerId, userId;
        String title, location, description, contact, type;
        LocalDateTime start, end;
        ResultSet apptQueryResult;
        String queryUserTail = Schedule.getCurrentUser().getId() + ";";
        String getApptsQuery = """
                SELECT *
                FROM appointments AS a
                JOIN contacts AS c
                ON a.contact_id = c.contact_id 
                WHERE a.user_id = """ + queryUserTail;

        DBConnector.connect();
        Query.runQuery(getApptsQuery);
        apptQueryResult = Query.getResults();

        System.out.println("Clearing appts...");
        Schedule.clearAppointments();

        while (apptQueryResult.next()) {
            id = apptQueryResult.getInt("appointment_id");
            customerId = apptQueryResult.getInt("customer_id");
            userId = apptQueryResult.getInt("user_id");
            title = apptQueryResult.getString("title");
            description = apptQueryResult.getString("description");
            location = apptQueryResult.getString("location");
            contact = apptQueryResult.getString("contact_name");
            type = apptQueryResult.getString("type");
            start = apptQueryResult
                    .getTimestamp("start")
                    .toLocalDateTime()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime();
            end = apptQueryResult
                    .getTimestamp("end")
                    .toLocalDateTime()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime();

            System.out.println("Creating appointment with ID: " + id);

            Appointment newAppt = new Appointment(id, title, description, location,
                    contact, type, start, end, customerId, userId);

            Schedule.addAppointment(newAppt);
        }

        DBConnector.closeConnection();
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
