package sample.utility;

import sample.model.Customer;
import sample.model.Manager;
import sample.model.SalesAssociate;

public class Session {
    private static Manager manager;
    private static SalesAssociate salesAssociate;
    private static Customer currentCustomer;

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

    public static int getSalesAssociateId() {
        return salesAssociate.getId();
    }
    public static int getCurrentCustomerId() {
        return currentCustomer.getId();
    }
}
