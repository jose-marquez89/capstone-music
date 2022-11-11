package sample.utility;

import java.util.ArrayList;

/**
 * Validates input for the add and update controllers
 * for customer data entry.
 *
 * @author Jose Marquez
 */
public class CustomerValidator {
    public static boolean validateTextFields(String[] textFields) {
        for (String field : textFields) {
            if (field == "") {
                return false;
            }
        }

        return true;
    }

    public static boolean validateDivision(Location div) {
        if (div == null) {
            return false;
        }

        return true;
    }
}
