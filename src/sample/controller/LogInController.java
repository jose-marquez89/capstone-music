package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.time.ZoneId;

public class LogInController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label zoneLabel;
    private ResultSet queryResult;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       String zone = ZoneId.systemDefault().getId();
       String formattedZone = "User Location: " + zone;
       zoneLabel.setText(formattedZone);
    }
    public void logIn(ActionEvent event) throws SQLException, IOException {
        Parent root;
        Stage stage;
        Scene scene;
        String candidatePword = passwordField.getText();
        String candidateUname = usernameField.getText();
        Alert badLogin = new Alert(Alert.AlertType.INFORMATION);

        badLogin.setTitle("Login");
        badLogin.setContentText("Username or password is incorrect");

        DBConnector.connect();
        Query.runQuery("SELECT user_name, password FROM users WHERE user_name = '" + candidateUname + "';");
        queryResult = Query.getResults();

        if (queryResult.next()) {
            if (candidatePword.equals(queryResult.getString("password"))) {
                root = FXMLLoader.load(getClass().getResource("../view/user-dashboard.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                return;
            }
        }

        badLogin.show();
    }
}
