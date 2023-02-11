package sample.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextInputValidator {

    public static boolean validatePrice(String price) {
        Pattern ptn = Pattern.compile("^\\d+\\.\\d{2}$");
        Matcher mo = ptn.matcher(price);
        boolean isMatch = mo.matches();

        return isMatch;
    }

    public static boolean validateInteger(String intInput) {
        Pattern ptn = Pattern.compile("^\\d+$");
        Matcher mo = ptn.matcher(intInput);
        boolean isMatch = mo.matches();

        return isMatch;
    }

    public static boolean validateEmail(String email) {
        Pattern ptn = Pattern.compile("[\\w\\d\\\\.]+@[\\w\\d]+(\\.\\w+)+");
        Matcher mo = ptn.matcher(email);
        boolean isMatch = mo.matches();

        return isMatch;
    }

    public static boolean validatePhone(String phone) {
        Pattern ptn = Pattern.compile("\\(\\d{3}\\)\\s\\d{3}\\-\\d{4}");
        Matcher mo = ptn.matcher(phone);
        boolean isMatch = mo.matches();

        return isMatch;
    }
}
