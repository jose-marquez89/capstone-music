package sample.utility;

import sample.model.*;

public class Session {
    private static Manager manager;
    private static SalesAssociate salesAssociate;
    private static Customer currentCustomer;
    private static Order currentOrder;
    private static Product currentProduct;

    public static void setManager(Manager manager) {
        Session.manager = manager;
    }

    public static void setSalesAssociate(SalesAssociate salesAssociate) {
        Session.salesAssociate = salesAssociate;
    }

    public static String getManagerName() {
        return manager.getName();
    }

    public static String getManagerUserName() {
        return manager.getUsername();
    }

    public static int getManagerStoreId() {
        return manager.getStoreId();
    }

    public static void setCurrentProduct(Product newProduct) {
       currentProduct = newProduct;
    }

    public static void setCurrentCustomer(Customer currentCustomer) {
        Session.currentCustomer = currentCustomer;
    }

    public static void setCurrentOrder(Order targetOrder) {
        currentOrder = targetOrder;
    }

    public static int getSalesAssociateId() {
        return salesAssociate.getId();
    }
    public static int getCurrentCustomerId() {
        return currentCustomer.getId();
    }

    public static String getCurrentCustomerName() {
        return currentCustomer.getName();
    }
    public static int getCurrentOrderId() {
        return currentOrder.getId();
    }

    public static Product getCurrentProduct() {
        return currentProduct;
    }
}
