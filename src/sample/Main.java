package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.utility.LogInLogger;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        // make static utilities available to application
        LogInLogger.initializeLogger();

        Parent root = FXMLLoader.load(getClass().getResource("view/log-in.fxml"));
        primaryStage.setTitle("POS/Inventory System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        root.requestFocus();
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
