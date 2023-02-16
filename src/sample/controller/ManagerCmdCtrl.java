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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.SalesAssociate;
import sample.utility.Session;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EventObject;
import java.util.ResourceBundle;

public class ManagerCmdCtrl implements Initializable {
    @FXML private Label managerLabel;
    @FXML private TableView<SalesAssociate> associateTbl;
    @FXML private TableColumn<SalesAssociate, String> associateNameCol;
    @FXML private TableColumn<SalesAssociate, LocalDateTime> associateStartDtCol;
    @FXML private ObservableList<SalesAssociate> associateContainer = FXCollections.observableArrayList();
    @FXML private Label storeLabel;
    private String message;
    private Parent root;
    private Scene scene;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String associateQuery, associateName;
        PreparedStatement associatePs;
        ResultSet associateRs;
        int empId, roleId, storeId;
        LocalDateTime startDate, endDate;

        managerLabel.setText("Session Manager: " + Session.getManagerName());
        roleId = 2; // magic sales associate number, I know
        storeId = Session.getManagerStoreId();

        // scan for available sales associates
        try {
            DBConnector.connect();
            associateQuery = """
                    SELECT * 
                    FROM employee
                    WHERE role_id = ?
                        AND store_id = ?
                        AND end_date IS NULL;
                    """;
            associatePs = Query.pendingStatement(associateQuery);
            associatePs.setInt(1, roleId);
            associatePs.setInt(2, storeId);
            associateRs = associatePs.executeQuery();

            while (associateRs.next()) {
                empId = associateRs.getInt("id");
                associateName = associateRs.getString("name");
                startDate = associateRs.getTimestamp("start_date").toLocalDateTime();
                try {
                    endDate = associateRs.getTimestamp("end_date").toLocalDateTime();
                } catch (NullPointerException npe) {
                    endDate = null;
                }
                associateContainer.add(new SalesAssociate(empId, associateName, startDate, endDate));
            }

            DBConnector.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // set up table columns
        associateNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associateStartDtCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        formatDateCol(associateStartDtCol);

        // load them into table
        associateTbl.setItems(associateContainer);

        storeLabel.setText("Store: " + Session.getCurrentStoreName());
    }

    public void formatDateCol(TableColumn col) {
        col.setCellFactory(cell -> new TableCell<SalesAssociate, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime dt, boolean empty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                super.updateItem(dt, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(dt));
                }
            }
        });
    }

    public void startSession(ActionEvent event) throws IOException {
        SelectionModel<SalesAssociate> sm = associateTbl.getSelectionModel();
        Alert noSelectionAlert = new Alert(Alert.AlertType.ERROR);

        noSelectionAlert.setTitle("Start Sales Associate Session");
        noSelectionAlert.setContentText("You must select a sales associate to start the POS session.");

        if (sm.isEmpty()) {
            noSelectionAlert.show();
            return;
        }

        Session.setSalesAssociate(sm.getSelectedItem());
        System.out.println("Selected SA: " + sm.getSelectedItem().getName());

        root = FXMLLoader.load(getClass().getResource("../view/pos.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("POS");
        scene = new Scene(root);
        stage.setScene(scene);
        root.requestFocus();
        stage.show();
    }

    public void viewReports(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/manager-reports.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Reports");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void manageInventory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/manage-inventory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Manage Inventory");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
