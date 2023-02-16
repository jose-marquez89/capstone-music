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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.dao.DBConnector;
import sample.dao.Query;
import sample.model.Item;
import sample.model.Product;
import sample.model.Service;
import sample.utility.Notification;
import sample.utility.Session;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

public class NewOrderCtrl implements Initializable {
    @FXML private TableView<Product> orderProductsTable;
    @FXML private TableColumn<Product, String> productNameCol;
    @FXML private TableColumn<Product, Double> productPriceCol;
    @FXML private TableColumn<Product, Integer> productStockCol;
    @FXML private TableView<Service> orderServiceTable;
    @FXML private TableColumn<Service, String> serviceNameCol;
    @FXML private TableColumn<Service, Double> servicePriceCol;
    @FXML private TableView<Item> orderItemsTable;
    @FXML private TableColumn<Item, String> orderNameCol;
    @FXML private TableColumn<Item, Double> orderPriceCol;
    @FXML private ObservableList<Product> productContainer = FXCollections.observableArrayList();
    @FXML private ObservableList<Service> serviceContainer = FXCollections.observableArrayList();
    @FXML private ObservableList<Item> orderLineContainer = FXCollections.observableArrayList();
    @FXML private ChoiceBox<String> serviceFilterCb;
    @FXML private Label totalDisplay, customerNameLabel;
    private ArrayList<String> serviceChoices;
    private ArrayList<Service> unfilteredServices;

    private Parent root;
    private Stage stage;
    private Scene scene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int id, stock, currentServiceId;
        double price;
        Service tempService = null;
        String productQuery, serviceQuery, name, associatedProduct, allProducts;
        ResultSet productRs, serviceRs;

        serviceChoices = new ArrayList<String>();
        serviceChoices.add("All Products");
        unfilteredServices = new ArrayList<Service>();
        productQuery = String.format("""
                SELECT
                	p.id,
                	p.name,
                	p.price,
                	i.on_hand
                FROM product p
                JOIN inventory i
                ON p.id = i.product_id
                JOIN store s
                ON i.store_id = s.id
                WHERE NOT discontinued
                	AND s.id = %d;
                """, Session.getManagerStoreId());

        serviceQuery = """
                SELECT
                	s.id,
                	s.name,
                	s.price,
                	p.name AS associated_product
                FROM service s
                JOIN service_application sa
                ON s.id = sa.service_id
                LEFT JOIN product p
                ON sa.product_id = p.id AND NOT p.discontinued
                WHERE NOT s.discontinued
                ORDER BY s.id;
                """;

        //initialize product columns
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("quantityOnHand"));

        // initialize service columns
        serviceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        servicePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // initialize order line columns
        orderNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        orderPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        try {
            DBConnector.connect();

            // load products
            Query.runQuery(productQuery);
            productRs = Query.getResults();

            while (productRs.next()) {
                id = productRs.getInt("id");
                name = productRs.getString("name");
                price = productRs.getDouble("price");
                stock = productRs.getInt("on_hand");

                productContainer.add(new Product(id, name, price, stock));
            }

            // load services
            Query.runQuery(serviceQuery);
            serviceRs = Query.getResults();
            currentServiceId = -1;

            while (serviceRs.next()) {
                id = serviceRs.getInt("id");
                name = serviceRs.getString("name");
                price = serviceRs.getDouble("price");
                associatedProduct = serviceRs.getString("associated_product");

                // only add unique products
                if (associatedProduct != null && !serviceChoices.contains(associatedProduct)) {
                    serviceChoices.add(associatedProduct);
                }

                if (id != currentServiceId) {
                    tempService = new Service(id, name, price);
                    unfilteredServices.add(tempService);
                    currentServiceId = id;
                }

                if (associatedProduct != null)
                    tempService.addAssociatedProduct(associatedProduct);
            }

            serviceFilterCb.getItems().addAll(serviceChoices);
            serviceFilterCb.setValue(serviceChoices.get(0));
            serviceContainer.addAll(unfilteredServices);
            orderProductsTable.setItems(productContainer);
            orderServiceTable.setItems(serviceContainer);
            orderItemsTable.setItems(orderLineContainer);

            totalDisplay.setText("$0.00");

            DBConnector.closeConnection();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        customerNameLabel.setText("Customer: " + Session.getCurrentCustomerName());
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

    public void filterServices(ActionEvent e) {
        String selectedProduct = serviceFilterCb.getSelectionModel().getSelectedItem();

        if (selectedProduct == "All Products") {
            serviceContainer.clear();
            serviceContainer.addAll(unfilteredServices);
            orderServiceTable.setItems(serviceContainer);

            return;
        }

        serviceContainer.clear();
        serviceContainer.addAll(
                unfilteredServices
                        .stream()
                        .filter(s -> s.getAssociatedProducts().contains(selectedProduct))
                        .toList());

        orderServiceTable.setItems(serviceContainer);
    }

    private class productIdComparator implements Comparator<Product> {

        public int compare(Product p1, Product p2) {
            if (p1.getId() == p2.getId())
                return 0;
            else if (p1.getId() > p2.getId())
                return 1;
            else
                return -1;
        }
    }

    private boolean validateOrder() {
        // TODO: finish validation - order products cannot exceed on hand qty
        int productId = -1, productCount = 0;
        ArrayList<Product> productsOnly = new ArrayList<Product>();

        // first you need to filter out services
        productsOnly.addAll(
                orderLineContainer
                        .stream()
                        .filter(i -> i instanceof Product)
                        .map(i -> (Product)i).toList());

        // sort products by id
        Collections.sort(productsOnly, new productIdComparator());

        for (Product p : productsOnly) {
            if (productId == p.getId())
                productCount++;
            else {
                productId = p.getId();
                productCount = 1;
            }

            if (productCount > p.getQuantityOnHand())
                return false;
        }

        return true;
    }

    public void submitOrder(ActionEvent e) throws SQLException, IOException {
        int customerId, salesAssociateId, storeId, newOrderId;
        PreparedStatement orderPs, orderLineBatchPs;
        ResultSet orderIdRs;
        String orderQuery, orderLineQuery, orderIdQuery;

        orderQuery = """
                INSERT INTO "order" (customer_id, processing_employee, origin_store_id)
                VALUES (?, ?, ?); 
                """;

        if (validateOrder()) {
            // kick off the order creation process
            customerId = Session.getCurrentCustomerId();
            salesAssociateId = Session.getSalesAssociateId();
            storeId = Session.getManagerStoreId();

            DBConnector.connect();
            orderPs = Query.pendingStatement(orderQuery);
            orderPs.setInt(1, customerId);
            orderPs.setInt(2, salesAssociateId);
            orderPs.setInt(3, storeId);

            orderPs.executeUpdate();

            /*
            get orderId - this would probably be better if the application kept track of order id
            as opposed to the DB keeping track as it is now.
             */
            orderIdQuery = """
                    SELECT MAX(id) AS max_id FROM "order";
                    """;
            Query.runQuery(orderIdQuery);
            orderIdRs = Query.getResults();

            if (orderIdRs.next()) {
                newOrderId = orderIdRs.getInt("max_id");
            } else {
                throw new RuntimeException("No order Id could be retrieved.");
            }

            // add the statements for order lines in a batch
            orderLineQuery = """
                        INSERT INTO order_line (is_service, product_id, service_id, order_id, sale_price)
                        VALUES (?, ?, ?, ?, ?);
                        """;

            orderLineBatchPs = Query.pendingStatement(orderLineQuery);

            for (Item i : orderLineContainer) {
                if (i instanceof Service) {
                    orderLineBatchPs.setBoolean(1, true);
                    orderLineBatchPs.setNull(2, Types.NULL);
                    orderLineBatchPs.setInt(3, i.getId());
                } else {
                    orderLineBatchPs.setBoolean(1, false);
                    orderLineBatchPs.setInt(2, i.getId());
                    orderLineBatchPs.setNull(3, Types.NULL);
                }

                orderLineBatchPs.setInt(4, newOrderId);
                orderLineBatchPs.setDouble(5, i.getPrice());
                orderLineBatchPs.addBatch();
            }

            orderLineBatchPs.executeBatch();
            Notification.orderSuccessConfirmation();
            switchForms(e, "pos.fxml", "POS");

        } else {
            Notification.inventoryError();
        }
    }

    private double getOrderTotal() {
        double total = 0.00;
        total = orderLineContainer.stream().mapToDouble(i -> i.getPrice()).reduce(0.0, (a, b) -> a + b);

        return total;
    }

    public void addProduct(ActionEvent e) {
        Product selected = orderProductsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Notification.noSelection("Product Selection", "product");
            return;
        }

        orderLineContainer.add(selected);
        totalDisplay.setText(String.format("$%.2f", getOrderTotal()));
    }

    public void addService(ActionEvent e) {
        Service selected = orderServiceTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Notification.noSelection("Service Selection", "service");
            return;
        }

        orderLineContainer.add(orderServiceTable.getSelectionModel().getSelectedItem());
        totalDisplay.setText(String.format("$%.2f", getOrderTotal()));
    }

    public void removeItem(ActionEvent e) {
        Item item = orderItemsTable.getSelectionModel().getSelectedItem();

        if (item == null) {
            Notification.noSelection("Order Item Selection", "line item");
            return;
        }
        orderLineContainer.remove(item);
        totalDisplay.setText(String.format("$%.2f", getOrderTotal()));
    }

    public void toPos(ActionEvent e) throws IOException {
        switchForms(e, "pos.fxml", "POS");
    }
}
