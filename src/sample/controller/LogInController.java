package sample.controller;

import javafx.event.ActionEvent;

import java.awt.*;

public class LogInController {
    private TextField usernameField;
    private TextField passwordField;
    private String candidateUser;
    public void logIn(ActionEvent event) {
        candidateUser = usernameField.getText();

        // query database for this user
            // if result is null display invalid user/password message
            // else compare entered password with password from entered user
    }
}
