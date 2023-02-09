package sample.utility;

import sample.model.Customer;
import sample.model.Manager;
import sample.model.Order;
import sample.model.SalesAssociate;

public class Session {
    private static Manager manager;
    private static SalesAssociate salesAssociate;
    private static Customer currentCustomer;
    private static Order currentOrder;

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
}
