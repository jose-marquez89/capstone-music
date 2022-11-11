package sample.utility;

import javafx.scene.control.Alert;

/**
 * Provides several methods for use in creating
 * alerts for different situations across the application.
 *
 * @author Jose Marquez
 */
public class Notification {
    public static void customerFieldsInvalid() {
        Alert invalidFields = new Alert(Alert.AlertType.ERROR);

        invalidFields.setTitle("Add/Update Customer");
        invalidFields.setContentText("One or more fields have no selection\nPlease make a selection or enter data for all fields.");
        invalidFields.show();
    }
    public static void customerDeleteConfirmation(String customerName) {
        Alert customerDeleted = new Alert(Alert.AlertType.INFORMATION);

        customerDeleted.setTitle("Update/Delete Customer");
        customerDeleted.setContentText(String.format("Customer %s has been deleted.", customerName));
        customerDeleted.show();

    }
    public static void customerHasAppointments() {
        Alert apptAlert = new Alert(Alert.AlertType.WARNING);

        apptAlert.setTitle("Update/Delete Customer");
        apptAlert.setContentText("You must delete all of this customer's appointments before attempting to delete.");
        apptAlert.show();
    }
    public static void noSelection(String title, String selectionObjectType) {
        Alert noSelectionAlert = new Alert(Alert.AlertType.INFORMATION);

        noSelectionAlert.setTitle(title);
        noSelectionAlert.setContentText(String.format("You must select the %s you would like to update.", selectionObjectType));
        noSelectionAlert.show();
    }
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
