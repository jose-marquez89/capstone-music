package sample.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
     * Alerts the sales associate that the return has been successfully submitted.
     */
    public static void returnSuccessConfirmation() {
        Alert orderSubmitted = new Alert(Alert.AlertType.INFORMATION);

        orderSubmitted.setTitle("New Return");
        orderSubmitted.setContentText("The return has been successfully submitted.");
        orderSubmitted.showAndWait();
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
     * Notifies the user of an overlapping appointment.
     */
    public static void inventoryError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Submit Order");
        alert.setContentText("One or more products exceeds current on hand inventory. Please review product lines.");

        alert.show();
    }

    public static boolean confirmRemove(String title, String itemType) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(String.format(
                "You are about to remove a %s. Removing it for your store removes it for all stores.\n\nAre you sure you want to continue?",
                itemType));

        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK)
            return true;
        else
            return false;
    }

    public static void blankName() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Blank Name Input");
        alert.setContentText("Name field cannot be blank.");

        alert.show();
    }

    public static void badNumericInput() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Malformed Numeric Input");
        alert.setContentText("One or more numeric fields contain malformed input. Format should be as follows:\n\nCurrency/Price Input: #.##\nQuantity Input: ###");

        alert.show();
    }

    public static void duplicateServiceAssociation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Existing Association");
        alert.setContentText("The selected product is already in the association list.");

        alert.show();
    }

    public static void noOrders() {
        Alert noStoreOrders = new Alert(Alert.AlertType.INFORMATION);

        noStoreOrders.setTitle("New Return");
        noStoreOrders.setContentText("This customer has no orders at this location.");
        noStoreOrders.showAndWait();
    }

    public static void emptyReturn() {
        Alert noStoreOrders = new Alert(Alert.AlertType.ERROR);

        noStoreOrders.setTitle("New Return");
        noStoreOrders.setContentText("The return list has no products. Add products from the existing order to continue.");
        noStoreOrders.showAndWait();
    }

    public static void notAProduct() {
        Alert noStoreOrders = new Alert(Alert.AlertType.INFORMATION);

        noStoreOrders.setTitle("Return Item");
        noStoreOrders.setContentText("The item you have selected is a service. Services cannot be restocked and are non-refundable.");
        noStoreOrders.showAndWait();
    }
    public static void emailPhoneError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Detail Input");
        alert.setContentText("Malformed phone or email input, please follow the formats below.\nPhone format: (###) ###-####\nEmail format: username@yourdomain.com");

        alert.show();
    }

    public static void genericInfoWait(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void genericInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);

        alert.show();
    }

    public static void genericErrorWait(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
