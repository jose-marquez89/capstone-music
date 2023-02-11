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
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Product;
import sample.model.Service;
import sample.utility.Notification;
import sample.utility.Session;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InvMgmtCtrl implements Initializable {
    @FXML TableView<Product> productTable;
    @FXML TableColumn<Product, String> productNameCol;
    @FXML TableColumn<Product, Double> productPriceCol;
    @FXML TableColumn<Product, Integer> productStockCol;
    @FXML TableView<Service> serviceTable;
    @FXML TableColumn<Service, String> serviceNameCol;
    @FXML TableColumn<Service, Double> servicePriceCol;
    @FXML ObservableList<Product> productContainer = FXCollections.observableArrayList();
    @FXML ObservableList<Service> serviceContainer = FXCollections.observableArrayList();
    private Parent root;
    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int id, stock;
        double price;
        String productQuery, serviceQuery, name;
        ResultSet itemsRs;

        // set up product columns
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("quantityOnHand"));

        // set up service columns
        serviceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        servicePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productQuery = String.format("""
                SELECT p.id, p.name, p.price, i.on_hand
                FROM product p
                JOIN inventory i
                ON p.id = i.product_id
                WHERE i.store_id = %d
                    AND NOT p.discontinued;
                """, Session.getManagerStoreId());

        serviceQuery = """
                SELECT id, name, price
                FROM service
                WHERE NOT discontinued;
                """;

        try {
            DBConnector.connect();
            Query.runQuery(productQuery);
            itemsRs = Query.getResults();

            while (itemsRs.next()) {
                id = itemsRs.getInt("id");
                name = itemsRs.getString("name");
                price = itemsRs.getDouble("price");
                stock = itemsRs.getInt("on_hand");

                productContainer.add(new Product(id, name, price, stock));
            }

            Query.runQuery(serviceQuery);
            itemsRs = Query.getResults();

            while (itemsRs.next()) {
                id = itemsRs.getInt("id");
                name = itemsRs.getString("name");
                price = itemsRs.getDouble("price");

                serviceContainer.add(new Service(id, name, price));
            }

            DBConnector.closeConnection();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        productTable.setItems(productContainer);
        serviceTable.setItems(serviceContainer);
    }

    public void addProduct(ActionEvent e) throws IOException {
        switchForms(e, "add-new-product.fxml", "New Product");
    }

    public void updateProduct(ActionEvent e) throws IOException {
        SelectionModel<Product> productSm;

        productSm = productTable.getSelectionModel();

        if (productSm.isEmpty()) {
            Notification.noSelection("Update Product", "product");
        } else {
            Session.setCurrentProduct(productSm.getSelectedItem());
            switchForms(e, "update-product.fxml", "Update Product");
        }
    }

    public void removeProduct(ActionEvent e) throws SQLException {
        String productQuery;
        Product selectedProduct;
        SelectionModel<Product> productSm;

        productSm = productTable.getSelectionModel();

        if (productSm.isEmpty()) {
            Notification.noSelection("Remove Product", "product");
        } else {
            selectedProduct = productSm.getSelectedItem();
            productQuery = String.format("""
                UPDATE product
                SET discontinued = TRUE
                WHERE id = %d; 
                """, selectedProduct.getId());

            DBConnector.connect();

            if (Notification.confirmRemove("Remove Product", "product")) {
                Query.runUpdate(productQuery);
                productContainer.remove(selectedProduct);
            }

            DBConnector.closeConnection();
        }
    }

    public void removeService(ActionEvent e) throws SQLException {
        String serviceQuery;
        Service selectedService;
        SelectionModel<Service> serviceSm;

        serviceSm = serviceTable.getSelectionModel();

        if (serviceSm.isEmpty()) {
            Notification.noSelection("Remove Service", "service");
        } else {
            selectedService = serviceSm.getSelectedItem();
            serviceQuery = String.format("""
                UPDATE service
                SET discontinued = TRUE
                WHERE id = %d; 
                """, selectedService.getId());

            DBConnector.connect();

            if (Notification.confirmRemove("Remove Service", "service")) {
                Query.runUpdate(serviceQuery);
                serviceContainer.remove(selectedService);
            }

            DBConnector.closeConnection();
        }
    }

    public void switchForms(ActionEvent e, String formName, String title) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../view/" + formName));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setTitle(title);
        scene = new Scene(root);
        stage.setScene(scene);
        root.requestFocus();
        stage.show();
    }

    public void toManagerConsole(ActionEvent event) throws IOException {
        switchForms(event, "manager-cmd-ctrl.fxml", "Manager Console");
    }
}
