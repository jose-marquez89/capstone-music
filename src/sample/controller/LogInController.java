package sample.controller;

import javafx.event.ActionEvent;
import sample.dao.DBConnector;
import sample.dao.Query;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInController {
    private TextField usernameField;
    private TextField passwordField;
    private ResultSet queryResult;
    private String candidateUser;
    public void logIn(ActionEvent event) throws SQLException {
        String enteredPword = passwordField.getText();
        candidateUser = usernameField.getText();

        // TODO: complete user authentication flow
        DBConnector.connect();
        Query.runQuery("SELECT user_name, password FROM users WHERE user_name = '" + candidateUser + "';");
        queryResult = Query.getResults();

        if (queryResult.first()) {
            // TODO: compare entered password to database password
        }
        // TODO: set logic for log-in failure
    }
}
