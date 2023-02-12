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
import sample.model.Service;
import sample.utility.Notification;
import sample.utility.Session;
import sample.utility.TextInputValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateServiceCtrl implements Initializable {
    @FXML TextField nameField;
    @FXML TextField priceField;
    @FXML TableView<Product> allProductsTable;
    @FXML TableColumn<Product, String> productNameCol;
    @FXML TableView<Product> associatedProductsTable;
    @FXML TableColumn<Product, String> associatedProductNameCol;
    @FXML ObservableList<Product> allProdsContainer = FXCollections.observableArrayList();
    @FXML ObservableList<Product> assProdsContainer = FXCollections.observableArrayList();
    private ArrayList<Product> removeQueue;
    private ArrayList<Product> addQueue;
    private ArrayList<Product> productReference;
    private ArrayList<Integer> associatedIds;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private Service selectedService;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int productId, stock;
        double productPrice;
        String productQuery, productName, assProductQuery, serviceName;
        ResultSet productRs, assProductRs;

        associatedIds = new ArrayList<>();
        removeQueue = new ArrayList<>();
        addQueue = new ArrayList<>();
        productReference = new ArrayList<>();

        selectedService = Session.getCurrentService();
        nameField.setText(selectedService.getName());
        priceField.setText(Double.toString(selectedService.getPrice()));

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

        assProductQuery = String.format("""
                SELECT p.id
                FROM product p
                JOIN inventory i
                ON p.id = i.product_id
                JOIN service_application s
                ON p.id = s.product_id
                WHERE NOT p.discontinued
                    AND i.store_id = %d
                    AND s.service_id = %d;
                """, Session.getManagerStoreId(), selectedService.getId());

        try {
            DBConnector.connect();
            Query.runQuery(productQuery);
            productRs = Query.getResults();

            // populate list of all products
            while (productRs.next()) {
                productId = productRs.getInt("id");
                productName = productRs.getString("name");
                productPrice = productRs.getDouble("price");
                stock = productRs.getInt("on_hand");

                allProdsContainer.add(new Product(productId, productName, productPrice, stock));
            }
            
            Query.runQuery(assProductQuery);
            assProductRs = Query.getResults();

            // populate currently associated products
            // this could be slow if there are many products to go through
            while (assProductRs.next()) {
                productId = assProductRs.getInt("id");
                associatedIds.add(productId);
            }

            for (Product p : allProdsContainer) {
                if (associatedIds.contains(p.getId())) {
                    assProdsContainer.add(p);
                    productReference.add(p);
                }
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

            // check if the service is already associated with the product
            if (!productReference.contains(selectedProduct))
                addQueue.add(selectedProduct);

            // check the removal container
            if (removeQueue.contains(selectedProduct))
                removeQueue.remove(selectedProduct);

            // make it visible
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

            // check if the product is already associated
            if (productReference.contains(selectedProduct))
                removeQueue.add(selectedProduct);

            // check the add container
            if (addQueue.contains(selectedProduct))
                addQueue.remove(selectedProduct);

            assProdsContainer.remove(selectedProduct);
        }
    }

    public void cancel(ActionEvent e) throws IOException {
        switchForms(e, "manage-inventory.fxml", "Inventory Management");
    }

    public void submitUpdate(ActionEvent e) throws SQLException, IOException {
        double newPrice;
        String newName, serviceQuery, addQuery, txtPrice, removeQuery;
        PreparedStatement servicePs, additionPs, removalPs;
        ResultSet idRs;

        // this is redundant if the values didn't change but for now it's ok
        serviceQuery = String.format("""
                UPDATE service
                SET name = ?, price = ?
                WHERE id = %d;
                """, selectedService.getId());

        addQuery = """
                INSERT INTO service_application (service_id, product_id)
                VALUES (?, ?);
                """;

        removeQuery = """
                DELETE FROM service_application 
                WHERE service_id = ? AND product_id = ?;
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

        // add new products
        additionPs = Query.pendingStatement(addQuery);

        for (Product p : addQueue) {
            additionPs.setInt(1, selectedService.getId());
            additionPs.setInt(2, p.getId());
            additionPs.addBatch();
        }

        additionPs.executeBatch();

        // remove existing products
        removalPs = Query.pendingStatement(removeQuery);

        for (Product p : removeQueue) {
            removalPs.setInt(1, selectedService.getId());
            removalPs.setInt(2, p.getId());
            removalPs.addBatch();
        }

        removalPs.executeBatch();

        Notification.genericInfoWait("Update Service", "The service has been updated.");
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
