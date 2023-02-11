package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.postgresql.util.PSQLException;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.utility.Notification;
import sample.utility.Session;
import sample.utility.TextInputValidator;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddProductCtrl {
    @FXML TextField nameField;
    @FXML TextField priceField;
    @FXML TextField quantityField;
    private Parent root;
    private Stage stage;
    private Scene scene;

    public void cancel(ActionEvent e) throws IOException {
       switchForms(e, "manage-inventory.fxml", "Return Config");
    }

    public void initOtherInventory(int newProductId) throws SQLException {
        int rowStoreId;
        String storeQuery, updateQuery;
        ResultSet storeRs;

        storeQuery = String.format("""
                SELECT id
                FROM store
                WHERE id <> %d;
                """, Session.getManagerStoreId());

        DBConnector.connect();
        Query.runQuery(storeQuery);
        storeRs = Query.getResults();

        while (storeRs.next()) {
            rowStoreId = storeRs.getInt("id");
            updateQuery = String.format("""
                    INSERT INTO inventory (product_id, store_id, on_hand)
                    VALUES (%d, %d, %d);
                    """, newProductId, rowStoreId, 0);

            Query.runUpdate(updateQuery);
        }
    }

    public void save(ActionEvent e) throws SQLException, IOException {
        String name, productQuery, inventoryQuery, newIdQuery, txtPrice, txtQty;
        PreparedStatement productPs, inventoryPs;
        ResultSet idRs;
        double price;
        int qtyOnHand, newProductId;

        name = nameField.getText();
        txtPrice = priceField.getText();
        txtQty = quantityField.getText();

        // validate before moving on
        if (name == "") {
            Notification.blankName();
            return;
        }

        if (!TextInputValidator.validatePrice(txtPrice) || !TextInputValidator.validateInteger(txtQty)) {
            Notification.badNumericInput();
            return;
        }

        price = Double.parseDouble(txtPrice);
        qtyOnHand = Integer.parseInt(txtQty);

        productQuery = """
                INSERT INTO product (name, price)
                VALUES (?, ?);
                """;

        inventoryQuery = """
                INSERT INTO inventory (product_id, store_id, on_hand)
                VALUES (?, ?, ?);
                """;

        DBConnector.connect();
        productPs = Query.pendingStatement(productQuery);
        productPs.setString(1, name);
        productPs.setDouble(2, price);
        try {
            productPs.executeUpdate();
        } catch (PSQLException sqlError) {
            Notification.genericErrorWait("New Product", "This product already exists. Add a model number or try a different name.");
            return;
        }

        // just like in the order controller, this would not be a good long term solution
        newIdQuery = """
                SELECT MAX(id) max_id
                FROM product;
                """;

        Query.runQuery(newIdQuery);
        idRs = Query.getResults();
        if (idRs.next()) {
            newProductId = idRs.getInt("max_id");
        } else {
            throw new RuntimeException("Database is not initialized!");
        }

        inventoryPs = Query.pendingStatement(inventoryQuery);
        inventoryPs.setInt(1, newProductId);
        inventoryPs.setInt(2, Session.getManagerStoreId());
        inventoryPs.setInt(3, qtyOnHand);
        inventoryPs.executeUpdate();

        initOtherInventory(newProductId);
        DBConnector.closeConnection();

        Notification.genericInfoWait("New Product", "New product was successfully added. Other stores now have access to this product, but inventory has been set to default of zero.");
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
