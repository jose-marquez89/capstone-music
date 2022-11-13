package sample.utility;

/**
 * Validates input for the add and update controllers
 * for customer data entry.
 *
 * @author Jose Marquez
 */
public class CustomerValidator {
    /**
     * Checks an array of text fields for emptiness.
     *
     * @param textFields an array of strings
     * @return true if none of the text fields are an empty string
     */
    public static boolean validateTextFields(String[] textFields) {
        for (String field : textFields) {
            if (field == "") {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks the contents of a <code>Location</code> object.
     *
     * If the object is empty (null), the customer being added/updated
     * will not be valid.
     *
     * @param div a <code>Location</code> object representing a division
     * @return true if the division exists
     */
    public static boolean validateDivision(Location div) {
        if (div == null) {
            return false;
        }

        return true;
    }
}
