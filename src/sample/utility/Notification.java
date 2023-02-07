package sample.utility;

import javafx.scene.control.Alert;

/**
 * Provides several methods for use in creating
 * alerts for different situations across the application.
 *
 * @author Jose Marquez
 */
public class Notification {
    /**
     * Notifies the user when customer fields have not been filled.
     */
    public static void customerFieldsInvalid() {
        Alert invalidFields = new Alert(Alert.AlertType.ERROR);

        invalidFields.setTitle("Add/Update Customer");
        invalidFields.setContentText("One or more fields have no selection\nPlease make a selection or enter data for all fields.");
        invalidFields.show();
    }

    /**
     * Alerts the sales associate that the order has been successfully submitted.
     */
    public static void orderSuccessConfirmation() {
        Alert orderSubmitted = new Alert(Alert.AlertType.INFORMATION);

        orderSubmitted.setTitle("New Order");
        orderSubmitted.setContentText("The order has been successfully submitted.");
        orderSubmitted.showAndWait();
    }

    /**
     * Warns the user that a customer cannot be deleted due to
     * existing appointments.
     */
    public static void customerHasAppointments() {
        Alert apptAlert = new Alert(Alert.AlertType.WARNING);

        apptAlert.setTitle("Update/Delete Customer");
        apptAlert.setContentText("You must delete all of this customer's appointments before attempting to delete.");
        apptAlert.show();
    }

    /**
     * Notifies the user that they have not made a selection in a <code>TableView</code>.
     *
     * @param title the title to display in the notification window
     * @param selectionObjectType the text object type to inject into the message
     */
    public static void noSelection(String title, String selectionObjectType) {
        Alert noSelectionAlert = new Alert(Alert.AlertType.INFORMATION);

        noSelectionAlert.setTitle(title);
        noSelectionAlert.setContentText(String.format("You must select a %s to continue.", selectionObjectType));
        noSelectionAlert.show();
    }

    /**
     * Notifies the user of an unfilled date.
     */
    public static void unfilledDate() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add/Update Appointment");
        alert.setContentText("One or more date/time fields have no selection.");

        alert.show();
    }

    /**
     * Notifies the user that no general contact has been selected.
     */
    public static void unfilledContacts() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add/Update Appointment");
        alert.setContentText("A selection must be made for Customer ID, User ID and Contact.");

        alert.show();
    }

    /**
     * Notifies the user that appointment start is not before appointment end.
     */
    public static void malformedAppointment() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add/Update Appointment");
        alert.setContentText("The appointment end date and time must be after the start date and time.");

        alert.show();
    }

    /**
     * Notifies the user of an overlapping appointment.
     */
    public static void inventoryError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Submit Order");
        alert.setContentText("One or more products exceeds current on hand inventory. Please review product lines.");

        alert.show();
    }

    /**
     * Notifies the user that selected appointment times are outside
     * of business hours.
     */
    public static void outOfBounds() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Add/Update Appointment");
        alert.setContentText("Selected appointment time falls outside of business hours. Hint: Check that your appointment does not span several days.\n\nBusiness hours: 8am-10pm EST");

        alert.show();
    }

    public static void blankName() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Blank Name Input");
        alert.setContentText("Name field cannot be blank. Please enter a name.");

        alert.show();
    }

    public static void emailPhoneError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Detail Input");
        alert.setContentText("Malformed phone or email input, please follow the formats below.\nPhone format: (###) ###-####\nEmail format: username@yourdomain.com");

        alert.show();
    }
}
