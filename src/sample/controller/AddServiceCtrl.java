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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Product;
import sample.utility.Notification;
import sample.utility.Session;
import sample.utility.TextInputValidator;

import javax.xml.validation.Validator;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddServiceCtrl implements Initializable {
    @FXML TextField nameField;
    @FXML TextField priceField;
    @FXML TableView<Product> allProductsTable;
    @FXML TableColumn<Product, String> productNameCol;
    @FXML TableView<Product> associatedProductsTable;
    @FXML TableColumn<Product, String> associatedProductNameCol;
    @FXML ObservableList<Product> allProdsContainer = FXCollections.observableArrayList();
    @FXML ObservableList<Product> assProdsContainer = FXCollections.observableArrayList();
    private Parent root;
    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int productId, stock;
        double productPrice;
        String productQuery, productName;
        ResultSet productRs;

        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        productQuery = String.format("""
                SELECT p.id, p.name, p.price, i.on_hand
                FROM product p
                JOIN inventory i
                ON p.id = i.product_id
                WHERE NOT p.discontinued
                    AND i.store_id = %d
                """, Session.getManagerStoreId());

        try {
            DBConnector.connect();
            Query.runQuery(productQuery);
            productRs = Query.getResults();

            while (productRs.next()) {
                productId = productRs.getInt("id");
                productName = productRs.getString("name");
                productPrice = productRs.getDouble("price");
                stock = productRs.getInt("on_hand");

                allProdsContainer.add(new Product(productId, productName, productPrice, stock));
            }

            DBConnector.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        allProductsTable.setItems(allProdsContainer);
        associatedProductsTable.setItems(assProdsContainer);
    }

    public void addToService(ActionEvent e) {
        SelectionModel<Product> productSm;
        Product selectedProduct;

        productSm = allProductsTable.getSelectionModel();

        if (productSm.isEmpty()) {
            Notification.noSelection("Product Association", "product");
        } else {
            selectedProduct = productSm.getSelectedItem();

            // don't add products that are already in the list
            if (assProdsContainer.contains(selectedProduct)) {
                Notification.duplicateServiceAssociation();
                return;
            }

            assProdsContainer.add(selectedProduct);
        }
    }

    public void removeFromService(ActionEvent e) {
        SelectionModel<Product> productSm;
        Product selectedProduct;

        productSm = associatedProductsTable.getSelectionModel();

        if (productSm.isEmpty()) {
            Notification.noSelection("Product Association", "product");
        } else {
            selectedProduct = productSm.getSelectedItem();
            assProdsContainer.remove(selectedProduct);
        }
    }

    public void cancel(ActionEvent e) throws IOException {
        switchForms(e, "manage-inventory.fxml", "Inventory Management");
    }

    public void submitNew(ActionEvent e) throws SQLException, IOException {
        int newId;
        double newPrice;
        String newName, serviceQuery, associationQuery, txtPrice, newIdQuery;
        PreparedStatement servicePs, associationPs;
        ResultSet idRs;

        serviceQuery = """
                INSERT INTO service (name, price)
                VALUES (?, ?);
                """;

        newIdQuery = """
                SELECT MAX(id) max_id
                FROM service;
                """;

        associationQuery = """
                INSERT INTO service_application (service_id, product_id)
                VALUES (?, ?);
                """;

        newName = nameField.getText();
        txtPrice = priceField.getText();

        if (newName.equals("")) {
            Notification.blankName();
            return;
        }

        if (!TextInputValidator.validatePrice(txtPrice)) {
            Notification.badNumericInput();
            return;
        } else {
            newPrice = Double.parseDouble(txtPrice);
        }

        DBConnector.connect();
        servicePs = Query.pendingStatement(serviceQuery);
        servicePs.setString(1, newName);
        servicePs.setDouble(2, newPrice);
        servicePs.executeUpdate();

        // not the best way to do this, may need to be refactored to properly scale
        Query.runQuery(newIdQuery);
        idRs = Query.getResults();

        if (idRs.next()) {
            newId = idRs.getInt("max_id");
        } else {
            throw new RuntimeException("Database is not populated!");
        }

        associationPs = Query.pendingStatement(associationQuery);

        for (Product p : assProdsContainer) {
            associationPs.setInt(1, newId);
            associationPs.setInt(2, p.getId());
            associationPs.addBatch();
        }

        associationPs.executeBatch();
        Notification.genericInfoWait("New Service", "The new service, including associated products, has been successfully added.");
        switchForms(e, "manage-inventory.fxml", "Manage Inventory");
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
}
