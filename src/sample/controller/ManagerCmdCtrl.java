package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import sample.utility.Session;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagerCmdCtrl implements Initializable {
    @FXML private Label managerLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        managerLabel.setText("Session Manager: " + Session.getManagerName());
    }
}
