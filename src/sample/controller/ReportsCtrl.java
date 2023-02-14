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
import sample.utility.ReportRow;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

public class ReportsCtrl implements Initializable {
    @FXML private TableView<ReportRow> reportTable;
    @FXML private TableColumn<ReportRow, String> customerCol;
    @FXML private TableColumn<ReportRow, String> storeCol;
    @FXML private TableColumn<ReportRow, String> cityCol;
    @FXML private TableColumn<ReportRow, String> stateCol;
    @FXML private TableColumn<ReportRow, Double> priceCol;
    @FXML private TableColumn<ReportRow, String> dateTimeCol;
    @FXML private RadioButton allTimeRadio;
    @FXML private RadioButton thisWeekRadio;
    @FXML private RadioButton thisMonthRadio;
    @FXML private ChoiceBox<String> storeCb;

    @FXML private ObservableList<ReportRow> reportDisplayContainer = FXCollections.observableArrayList();
    @FXML private Label grandTotalDisplay;
    private ArrayList<ReportRow> unfilteredRows;
    private ArrayList<String> storeChoices;
    private Parent root;
    private Scene scene;
    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String reportQuery, customerName, storeName, city, state;
        double orderTotal;
        LocalDateTime createdAt;
        ResultSet reportRs;

        // initialize columns
        customerCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateTimeCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        storeCol.setCellValueFactory(new PropertyValueFactory<>("store"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("salePrice"));

        unfilteredRows = new ArrayList<>();
        storeChoices = new ArrayList<>();
        storeChoices.add("All");

        reportQuery = """
                WITH all_orders AS (
                	SELECT
                		c.name AS customer_name,
                		o.created_at,
                		o.id AS order_id,
                		s.name AS store_name,
                		s.city,
                		s.state,
                		o.origin_store_id
                	FROM customer AS c
                	JOIN "order" AS o
                	ON c.id = o.customer_id
                	JOIN store AS s
                	ON o.origin_store_id = s.id
                ),
                                
                agg AS (
                	SELECT
                		order_id,
                		SUM(sale_price) AS order_total
                	FROM order_line
                	WHERE NOT "return"
                	GROUP BY order_id
                )
                                
                SELECT
                	o.customer_name,
                	o.created_at,
                	o.store_name,
                	o.city,
                	o.state,
                	a.order_total
                FROM all_orders AS o
                JOIN agg AS a
                ON o.order_id = a.order_id;
                """;

        try {
            DBConnector.connect();
            Query.runQuery(reportQuery);
            reportRs = Query.getResults();

            while (reportRs.next()) {
                customerName = reportRs.getString("customer_name");
                createdAt = reportRs.getTimestamp("created_at").toLocalDateTime();
                storeName = reportRs.getString("store_name");
                city = reportRs.getString("city");
                state = reportRs.getString("state");
                orderTotal = reportRs.getDouble("order_total");

                if (!storeChoices.contains(storeName))
                    storeChoices.add(storeName);

                unfilteredRows.add(new ReportRow(customerName, storeName, city, state, createdAt, orderTotal));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Collections.sort(storeChoices);
        storeCb.getItems().addAll(storeChoices);
        storeCb.setValue(storeChoices.get(0));
        allTimeRadio.setSelected(true);
        reportDisplayContainer.addAll(unfilteredRows);
        reportTable.setItems(reportDisplayContainer);

        // set total display
        grandTotalDisplay.setText(String.format("$%.2f", getGrandTotal(unfilteredRows)));
    }

    private double getGrandTotal(List<ReportRow> reportsList) {
        double total = 0.00;
        total = reportsList.stream().mapToDouble(i -> i.getSalePrice()).reduce(0.0, (a, b) -> a + b);

        return total;
    }

    public void filterReport(ActionEvent e) {
        int currentWeek, currentMonth;
        String choiceBoxItem, selectedStore;
        LocalDateTime rightNow;
        WeekFields localWf;
        ArrayList<ReportRow> tempList;
        reportDisplayContainer.clear();

        choiceBoxItem = storeCb.getSelectionModel().getSelectedItem();
        selectedStore = choiceBoxItem;

        if (allTimeRadio.isSelected() && selectedStore.equals("All")) {
            reportDisplayContainer.addAll(unfilteredRows);
            grandTotalDisplay.setText(String.format("$%.2f", getGrandTotal(reportDisplayContainer)));
            return;
        }

        tempList = new ArrayList<>();
        localWf = WeekFields.of(Locale.getDefault());
        rightNow = LocalDateTime.now();
        currentWeek = rightNow.get(localWf.weekOfWeekBasedYear());
        currentMonth = rightNow.getMonthValue();

        if (thisWeekRadio.isSelected()) {
            tempList.addAll(unfilteredRows
                    .stream()
                    .filter(r -> r.getCreatedAt().get(localWf.weekOfWeekBasedYear()) == currentWeek)
                    .toList());
        } else if (thisMonthRadio.isSelected()) {
            tempList.addAll(unfilteredRows
                    .stream()
                    .filter(r -> r.getCreatedAt().getMonthValue() == currentMonth)
                    .toList());
        } else {
            tempList.addAll(unfilteredRows);
        }

        if (choiceBoxItem != "All") {
            reportDisplayContainer.addAll(
                    tempList
                            .stream()
                            .filter(r -> r.getStore().equals(selectedStore))
                            .toList()
            );
        } else {
            reportDisplayContainer.addAll(tempList);
        }

        grandTotalDisplay.setText(String.format("$%.2f", getGrandTotal(reportDisplayContainer)));
    }

    public void toManagerConsole(ActionEvent e) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/manager-cmd-ctrl.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setTitle("Manager Console");
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
