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
import sample.model.Manager;
import sample.utility.LogInLogger;
import sample.utility.Session;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
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
    private ResultSet queryResult, mgrResult;
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
        int id, storeId;
        Manager sessionManager = null;
        String username, name, createdBy, managerDetailsQ, storeName;
        LocalDateTime startDate, endDate;
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
        Query.runQuery("SELECT * FROM manager_detail WHERE user_name = '" + candidateUname + "';");
        queryResult = Query.getResults();

        if (queryResult.next()) {
            // we really don't need the User class
            id = Integer.parseInt(queryResult.getString("employee_id"));
            username = queryResult.getString("user_name");

            if (candidatePword.equals(queryResult.getString("password"))) {
                // create the session's manager
                managerDetailsQ = """
                        SELECT e.*, m.user_name, s.name AS store_name
                        FROM employee AS e
                        JOIN manager_detail AS m
                        ON e.id = m.employee_id
                        JOIN store AS s
                        ON e.store_id = s.id
                        WHERE e.id = ?;
                        """;

                PreparedStatement managerPs = Query.pendingStatement(managerDetailsQ);
                managerPs.setInt(1, id);

                mgrResult = managerPs.executeQuery();

                if (mgrResult.next()) {
                    name = mgrResult.getString("name");
                    storeId = mgrResult.getInt("store_id");
                    startDate = mgrResult.getTimestamp("start_date").toLocalDateTime();
                    storeName = mgrResult.getString("store_name");

                    try {
                        endDate = mgrResult.getTimestamp("end_date").toLocalDateTime();
                    } catch (NullPointerException npe){
                        endDate = null;
                    }
                    sessionManager = new Manager(id, name, startDate, endDate, candidateUname, storeId);
                    Session.setManager(sessionManager);
                    Session.setStore(storeName);
                } else {
                    LogInLogger.getLogger().info("Could not set a manager for the current session, check database.");
                }

                // Schedule.setCurrentUser(queuedUser);
                root = FXMLLoader.load(getClass().getResource("../view/manager-cmd-ctrl.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setTitle("Manager Console");
                stage.setScene(scene);
                root.requestFocus();
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
