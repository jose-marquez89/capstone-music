package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.postgresql.util.PSQLException;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Product;
import sample.utility.Notification;
import sample.utility.Session;
import sample.utility.TextInputValidator;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateProductCtrl implements Initializable {
    @FXML TextField nameField;
    @FXML TextField priceField;
    @FXML TextField quantityField;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private int existingProductId;
    private Product selectedProduct;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String prvName, prvPrice, prvQty;

        selectedProduct = Session.getCurrentProduct();
        existingProductId = selectedProduct.getId();
        prvName = selectedProduct.getName();
        prvPrice = Double.toString(selectedProduct.getPrice());
        prvQty = Integer.toString(selectedProduct.getQuantityOnHand());

        nameField.setText(prvName);
        priceField.setText(prvPrice);
        quantityField.setText(prvQty);
    }

    public void cancel(ActionEvent e) throws IOException {
       switchForms(e, "manage-inventory.fxml", "Return Config");
    }

    public void save(ActionEvent e) throws SQLException, IOException {
        String name, productQuery, inventoryQuery, txtPrice, txtQty;
        PreparedStatement productPs, inventoryPs;
        double price;
        int qtyOnHand;

        name = nameField.getText();
        txtPrice = priceField.getText();
        txtQty = quantityField.getText();

        // validate before moving on
        if (name.equals("")) {
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
                UPDATE product
                SET name = ?, price = ?
                WHERE id = ?;
                """;

        inventoryQuery = """
                UPDATE inventory
                SET on_hand = ?
                WHERE product_id = ? AND store_id = ?
                """;

        DBConnector.connect();
        productPs = Query.pendingStatement(productQuery);
        productPs.setString(1, name);
        productPs.setDouble(2, price);
        productPs.setInt(3, selectedProduct.getId());

        try {
            productPs.executeUpdate();
        } catch (PSQLException sqlError) {
            Notification.genericErrorWait("New Product", "This product already exists. Add a model number or try a different name.");
            return;
        }

        inventoryPs = Query.pendingStatement(inventoryQuery);
        inventoryPs.setInt(1, qtyOnHand);
        inventoryPs.setInt(2, existingProductId);
        inventoryPs.setInt(3, Session.getManagerStoreId());
        inventoryPs.executeUpdate();

        DBConnector.closeConnection();

        Notification.genericInfoWait("Update Product", "The product was successfully updated.");
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
