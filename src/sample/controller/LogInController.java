package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.ZoneId;

public class LogInController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label zoneLabel;
    @FXML private Button logInButton;
    private ResultSet queryResult;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ResourceBundle localeBundle = ResourceBundle.getBundle("Nat", Locale.getDefault());
        String zone = ZoneId.systemDefault().getId();
        String formattedZone = localeBundle.getString("userLocation") + " " + zone;
        String unamePrompt = localeBundle.getString("username");
        String pwordPrompt = localeBundle.getString("password");
        String logInBtnText = localeBundle.getString("logIn");

        zoneLabel.setText(formattedZone);
        usernameField.setPromptText(unamePrompt);
        passwordField.setPromptText(pwordPrompt);
        logInButton.setText(logInBtnText);
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
