package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.SalesAssociate;
import sample.utility.Session;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ManagerCmdCtrl implements Initializable {
    @FXML private Label managerLabel;
    @FXML private TableView associateTbl;
    @FXML private TableColumn<SalesAssociate, String> associateNameCol;
    @FXML private TableColumn<SalesAssociate, LocalDateTime> associateStartDtCol;
    @FXML private ObservableList<SalesAssociate> associateContainer = FXCollections.observableArrayList();

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

        // load them into table
        associateTbl.setItems(associateContainer);
    }
}
