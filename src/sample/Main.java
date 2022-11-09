package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.utility.DisplayContacts;
import sample.utility.DisplayLocations;
import sample.utility.DisplayMinutes;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        // make static utilities available to application
        DisplayMinutes.initializeMinutes();
        DisplayLocations.initializeLocations();
        DisplayContacts.initializeContacts();

        Parent root = FXMLLoader.load(getClass().getResource("view/log-in.fxml"));
        primaryStage.setTitle("Scheduler Pro");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        root.requestFocus();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
