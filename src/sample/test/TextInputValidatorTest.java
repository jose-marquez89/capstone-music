package sample.test;

import org.junit.jupiter.api.Test;
import sample.utility.TextInputValidator;

import static org.junit.jupiter.api.Assertions.*;

class TextInputValidatorTest {

    @Test
    void validateEmail() {
        String testEmail1, testEmail2, testEmail3, testEmail4;

        testEmail1 = "jose.marquez@brown.co.uk";
        testEmail2 = "robert_johnson34@yahoo.com";
        testEmail3 = "invalid_email#email.com";
        testEmail4 = "notAn Emal At All";

        assertTrue(TextInputValidator.validateEmail(testEmail1));
        assertTrue(TextInputValidator.validateEmail(testEmail2));
        assertFalse(TextInputValidator.validateEmail(testEmail3));
        assertFalse(TextInputValidator.validateEmail(testEmail4));
    }

    @Test
    void validatePhone() {
        String testPhone1, testPhone2, testPhone3, testPhone4, testPhone5;

        testPhone1 = "(210) 456-6785";
        testPhone2 = "(345 345-4955";
        testPhone3 = "345-3456";
        testPhone4 = "123 234 3456";
        testPhone5 = "1233455432";

        assertTrue(TextInputValidator.validatePhone(testPhone1));
        assertFalse(TextInputValidator.validatePhone(testPhone2));
        assertFalse(TextInputValidator.validatePhone(testPhone3));
        assertFalse(TextInputValidator.validatePhone(testPhone4));
        assertFalse(TextInputValidator.validatePhone(testPhone5));
    }
}