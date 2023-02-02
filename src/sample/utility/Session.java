package sample.utility;

import sample.model.Manager;
import sample.model.SalesAssociate;

public class Session {
    private static Manager manager;
    private static SalesAssociate salesAssociate;

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
}
