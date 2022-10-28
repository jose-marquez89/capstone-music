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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class UserDashboardController implements Initializable {
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> customerIdCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerAddressCol;
    @FXML private TableColumn<Customer, String> postalCodeCol;
    @FXML private TableColumn<Customer, String> phoneNumberCol;
    @FXML private TableColumn<Customer, String> customerDivisionCol;
    @FXML private TableColumn<Customer, String> customerDivCountryCol;

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
            divId = customerQueryResult.getString("division_id");
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
        ZonedDateTime start, end;
        ResultSet apptQueryResult;
        String queryUserTail = Schedule.getCurrentUser().getName() + "';";
        String getApptsQuery = """
                SELECT *
                FROM appointments AS a
                JOIN contacts AS c
                ON a.contact_id = c.contact_id 
                WHERE a.user_id = '""" + queryUserTail;

        DBConnector.connect();
        Query.runQuery(getApptsQuery);
        apptQueryResult = Query.getResults();

        Schedule.clearAppointments();

        while (apptQueryResult.next()) {
            id = apptQueryResult.getInt("appointment_id");
            customerId = apptQueryResult.getInt("customer_id");
            userId = apptQueryResult.getInt("user_id");
            title = apptQueryResult.getString("title");
            description = apptQueryResult.getString("description");
            location = apptQueryResult.getString("location");
            contact = apptQueryResult.getString("contact");
            type = apptQueryResult.getString("type");
            start = apptQueryResult
                    .getTimestamp("start")
                    .toLocalDateTime()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault());
            end = apptQueryResult
                    .getTimestamp("end")
                    .toLocalDateTime()
                    .atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(ZoneId.systemDefault());

            Appointment newAppt = new Appointment(id, title, description, location,
                    contact, type, start, end, customerId, userId);

            Schedule.addAppointment(newAppt);
        }

        DBConnector.closeConnection();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initializing user dashboard view...");
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

        try {
            updateCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        customerTable.setItems(Schedule.getCustomers());
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
