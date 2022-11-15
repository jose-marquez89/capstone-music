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
import sample.model.Schedule;
import sample.model.User;
import sample.utility.LogInLogger;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.ZoneId;

/**
 * Controls the form responsible for logging in authorized users.
 *
 * Allows authorized users to access the application via text entry of
 * usernames and passwords. The controller also records all login attempts
 * to the `login_activity.txt` file.
 *
 * @author Jose Marquez
 */
public class LogInController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label zoneLabel;
    @FXML private Button logInButton;
    private ResultSet queryResult;
    private Parent root;
    private Stage stage;
    private Scene scene;
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
        User queuedUser;
        int id;
        String name, createdBy, lastUpdatedBy;
        LocalDateTime createdDate, lastUpdatedDate;
        String candidatePword = passwordField.getText();
        String candidateUname = usernameField.getText();
        ResourceBundle localeBundle = ResourceBundle.getBundle("Nat", Locale.getDefault());
        ZoneId systemZone = ZoneId.systemDefault();
        String alertMessage = localeBundle.getString("badLogin");
        String alertTitle = localeBundle.getString("logInTitle");
        Alert badLogin = new Alert(Alert.AlertType.INFORMATION);

        badLogin.setTitle(alertTitle);
        badLogin.setContentText(alertMessage);

        DBConnector.connect();
        Query.runQuery("SELECT * FROM users WHERE user_name = '" + candidateUname + "';");
        queryResult = Query.getResults();

        if (queryResult.next()) {
            // we really don't need the User class
            id = Integer.parseInt(queryResult.getString("user_id"));
            name = queryResult.getString("user_name");
            createdDate = queryResult.getTimestamp("create_date")
                    .toLocalDateTime();
            lastUpdatedDate = queryResult.getTimestamp("create_date")
                    .toLocalDateTime();
            createdBy = queryResult.getString("created_by");
            lastUpdatedBy = queryResult.getString("last_updated_by");
            queuedUser = new User(id, name, createdDate,createdBy, lastUpdatedDate, lastUpdatedBy);

            if (candidatePword.equals(queryResult.getString("password"))) {
                queuedUser.setLogInDateTime();
                Schedule.setCurrentUser(queuedUser);
                root = FXMLLoader.load(getClass().getResource("../view/user-dashboard.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.setX(100.0);
                stage.setY(5.0);
                stage.show();

                DBConnector.closeConnection();
                LogInLogger.getLogger().info("Login succeeded for user candidate: " + candidateUname);
                LogInLogger.closeLoggingHandler();

                return;
            }
        }

        DBConnector.closeConnection();
        LogInLogger.getLogger().info("Login failed for user candidate: " + candidateUname);
        badLogin.show();
    }
}
