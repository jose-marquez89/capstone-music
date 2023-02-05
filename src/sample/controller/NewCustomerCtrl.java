package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.utility.Notification;
import sample.utility.TextInputValidator;

import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewCustomerCtrl {
    @FXML TextField customerNameField;
    @FXML TextField customerEmailField;
    @FXML TextField customerPhoneField;
    private Parent root;
    private Stage stage;
    private Scene scene;

    public void switchForms(ActionEvent e, String formName, String title) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/" + formName));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setTitle(title);
        scene = new Scene(root);
        stage.setScene(scene);
        root.requestFocus();
        stage.show();
    }

    public void saveNewCustomer(ActionEvent e) throws SQLException, IOException {
        String name, email, phone, newCustomerQuery;
        PreparedStatement newCustomerPs;

        newCustomerQuery = """
                INSERT INTO customer (name, email, phone)
                VALUES (?, ?, ?); 
                """;

        name = customerNameField.getText();
        email = customerEmailField.getText();
        phone = customerPhoneField.getText();

        if (name == "") {
            Notification.blankName();
            return;
        } else if (!TextInputValidator.validateEmail(email) || !TextInputValidator.validatePhone(phone)) {
            Notification.emailPhoneError();
            return;
        } else {
            DBConnector.connect();
            newCustomerPs = Query.pendingStatement(newCustomerQuery);
            newCustomerPs.setString(1, name);
            newCustomerPs.setString(2, email);
            newCustomerPs.setString(3, phone);
            newCustomerPs.executeUpdate();
        }

        switchForms(e, "pos.fxml", "POS");
    }

    public void cancel(ActionEvent e) throws IOException {
        switchForms(e, "pos.fxml", "POS");
    }
}
