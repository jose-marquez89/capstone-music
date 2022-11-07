package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.utility.Country;
import sample.utility.DisplayLocations;
import sample.utility.Division;
import sample.utility.Location;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class CustomerAddController implements Initializable {
    @FXML private ComboBox<Location> countrySelector;
    @FXML private ComboBox<Location> divisionSelector;
    private ObservableList<Division> divisionContainer = FXCollections.observableArrayList();
    Parent root;
    Scene scene;
    Stage stage;

    public void setComboBoxDisplay(ComboBox<Location> cb) {
        cb.setCellFactory(cell -> new ListCell<Location>() {
            @Override
            protected void updateItem(Location l, boolean empty) {
                super.updateItem(l, empty);
                setText(empty ? null : l.getName());
            }
        });

        cb.setButtonCell(new ListCell<Location>() {
            @Override
            protected void updateItem(Location l, boolean empty) {
                super.updateItem(l, empty);
                setText((empty) ? null : l.getName());
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setComboBoxDisplay(countrySelector);
        setComboBoxDisplay(divisionSelector);
        countrySelector.getItems().addAll(DisplayLocations.getCountries());
    }

    public void filterDivision() {
        int countryId;
        if (countrySelector.getValue() != null) {
            countryId = countrySelector.getValue().getId();
            Stream<Division> divisionStream = DisplayLocations.getDivisions().stream();
            List<Division> filteredDivisions = divisionStream.filter(div -> div.getCountryId() == countryId).toList();
            divisionSelector.getItems().clear();
            divisionSelector.getItems().addAll(filteredDivisions);
        }
    }

    public void mainFormRedirect(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/user-dashboard.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setX(100.0);
        stage.setY(50.0);
        stage.show();
    }
}
