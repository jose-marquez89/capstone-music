package sample.utility;

import java.util.ArrayList;

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
