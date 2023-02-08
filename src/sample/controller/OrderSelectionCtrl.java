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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Order;
import sample.utility.Notification;
import sample.utility.Session;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class OrderSelectionCtrl implements Initializable {
    @FXML TableView<Order> returnOrderTable;
    @FXML TableColumn<Order, Integer> returnOrderIdCol;
    @FXML TableColumn<Order, LocalDateTime> returnOrderDtCol;
    @FXML Label customerNameLabel;
    @FXML ObservableList<Order> orderContainer = FXCollections.observableArrayList();
    private Parent root;
    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int orderId;
        LocalDateTime createdAt;
        String orderQuery;
        PreparedStatement orderPs;
        ResultSet orderRs;

        returnOrderIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        returnOrderDtCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        formatDateCol(returnOrderDtCol);

        orderQuery = """
                SELECT id, created_at
                FROM "order"
                WHERE customer_id = ?
                    AND origin_store_id = ?; 
                """;

        try {
            DBConnector.connect();
            orderPs = Query.pendingStatement(orderQuery);
            orderPs.setInt(1, Session.getCurrentCustomerId());
            orderPs.setInt(2, Session.getManagerStoreId());
            orderRs = orderPs.executeQuery();

            while (orderRs.next()) {
                orderId = orderRs.getInt("id");
                createdAt = orderRs.getTimestamp("created_at").toLocalDateTime();
                System.out.println(orderId);
                System.out.println(createdAt);

                orderContainer.add(new Order(orderId, createdAt));
            }

            returnOrderTable.setItems(orderContainer);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        customerNameLabel.setText("Customer: " + Session.getCurrentCustomerName());
    }

    public void formatDateCol(TableColumn col) {
        col.setCellFactory(cell -> new TableCell<Order, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime dt, boolean empty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
                super.updateItem(dt, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(dt));
                }
            }
        });
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

    public void cancel(ActionEvent e) throws IOException {
        switchForms(e, "pos.fxml", "POS");
    }
}
