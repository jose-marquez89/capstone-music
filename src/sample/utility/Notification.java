package sample.utility;

import javafx.scene.control.Alert;

public class Notification {
    public static void unfilledDate() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add/Update Appointment");
        alert.setContentText("One or more date/time fields have no selection.");

        alert.show();
    }

    public static void unfilledContacts() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add/Update Appointment");
        alert.setContentText("A selection must be made for Customer ID, User ID and Contact.");

        alert.show();
    }
    public static void malformedAppointment() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add/Update Appointment");
        alert.setContentText("The appointment end date and time must be after the start date and time.");

        alert.show();
    }

    public static void overlappingAppointment() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add/Update Appointment");
        alert.setContentText("The selected time slot overlaps with one or more existing appointments, please select another time.");

        alert.show();
    }

    public static void outOfBounds() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add/Update Appointment");
        alert.setContentText("Selected appointment time falls outside of business hours. Hint: Check that your appointment does not span several days.\n\nBusiness hours: 8am-10pm EST");

        alert.show();
    }
}
