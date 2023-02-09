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
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.*;
import sample.utility.Notification;
import sample.utility.Session;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReturnConfigCtrl implements Initializable {
    @FXML private TableView<OrderLine> orderItemTable;
    @FXML private TableColumn<OrderLine, String> orderItemCol;
    @FXML private TableColumn<OrderLine, Double> orderItemSalePrice;
    @FXML private TableView<OrderLine> returnTable;
    @FXML private TableColumn<OrderLine, String> returnItemCol;
    @FXML private TableColumn<OrderLine, Double> returnValueCol;
    @FXML private ObservableList<OrderLine> orderItemContainer = FXCollections.observableArrayList();
    @FXML private ObservableList<OrderLine> returnItemContainer = FXCollections.observableArrayList();
    @FXML private Label returnValueLabel;
    private Parent root;
    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int itemId;
        double itemPrice;
        boolean isService;
        String orderOrderLineQuery, itemName;
        ResultSet orderOrderLineRs;

        orderItemCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        orderItemSalePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        returnItemCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        returnValueCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        orderOrderLineQuery = String.format("""
                SELECT ol.id, COALESCE(p.name, s.name) AS name, ol.sale_price, ol.is_service
                FROM order_line ol
                LEFT JOIN product p
                ON ol.product_id = p.id
                LEFT JOIN service s
                ON ol.service_id = s.id
                WHERE order_id = %d
                    AND NOT ol.return;
                """, Session.getCurrentOrderId());

        try {
            DBConnector.connect();
            Query.runQuery(orderOrderLineQuery);
            orderOrderLineRs = Query.getResults();

            while (orderOrderLineRs.next()) {
                itemId = orderOrderLineRs.getInt( "id");
                itemName = orderOrderLineRs.getString("name");
                itemPrice = orderOrderLineRs.getDouble("sale_price");
                isService = orderOrderLineRs.getBoolean("is_service");

                orderItemContainer.add(new OrderLine(itemId, itemName, itemPrice, isService));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        orderItemTable.setItems(orderItemContainer);
        returnTable.setItems(returnItemContainer);
        returnValueLabel.setText("$0.00");
    }

    public void addReturnItem(ActionEvent e) {
        SelectionModel<OrderLine> itemSelectionModel;
        OrderLine selectedOrderLine;

        itemSelectionModel = orderItemTable.getSelectionModel();

        if (itemSelectionModel.isEmpty()) {
            Notification.noSelection("Return OrderLine", "order item");
        } else {

            if (itemSelectionModel.getSelectedItem().isService()) {
                Notification.notAProduct();
                return;
            }

            selectedOrderLine = itemSelectionModel.getSelectedItem();
            orderItemContainer.remove(selectedOrderLine);
            returnItemContainer.add(selectedOrderLine);
            returnValueLabel.setText(String.format("$%.2f", getReturnTotal()));
        }
    }

    private double getReturnTotal() {
        double total = 0.00;
        total = returnItemContainer.stream().mapToDouble(i -> i.getPrice()).reduce(0.0, (a, b) -> a + b);

        return total;
    }

    public void removeReturnItem(ActionEvent e) {
        SelectionModel<OrderLine> itemSelectionModel;
        OrderLine selectedOrderLine;

        itemSelectionModel = returnTable.getSelectionModel();

        if (itemSelectionModel.isEmpty()) {
            Notification.noSelection("Remove Return OrderLine", "pending return item");
        } else {
            selectedOrderLine = itemSelectionModel.getSelectedItem();
            returnItemContainer.remove(selectedOrderLine);
            orderItemContainer.add(selectedOrderLine);
            returnValueLabel.setText(String.format("$%.2f", getReturnTotal()));
        }
    }

    public void submitReturn(ActionEvent e) throws SQLException, IOException {
        String returnQuery, orderQuery;
        PreparedStatement returnPs;

        if (returnItemContainer.isEmpty()) {
            Notification.emptyReturn();
            return;
        }

        returnQuery = """
                UPDATE order_line
                SET return = TRUE, returned_at = NOW()
                WHERE id = ?; 
                """;

        orderQuery = String.format("""
                UPDATE "order"
                SET has_return = TRUE
                WHERE id = %d;
                """, Session.getCurrentOrderId());

        DBConnector.connect();
        Query.runUpdate(orderQuery);
        returnPs = Query.pendingStatement(returnQuery);

        for (OrderLine line : returnItemContainer) {
            returnPs.setInt(1, line.getId());
            returnPs.addBatch();
        }

        returnPs.executeBatch();
        Notification.returnSuccessConfirmation();
        switchForms(e, "pos.fxml", "POS");
    }

    public void cancel(ActionEvent e) throws IOException {
        switchForms(e, "pos.fxml", "POS");
    }

    private void switchForms(ActionEvent e, String formName, String title) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/" + formName));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setTitle(title);
        scene = new Scene(root);
        stage.setScene(scene);
        root.requestFocus();
        stage.show();
    }
}
