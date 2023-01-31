package sample.model;

import java.time.LocalDateTime;

public class Manager extends Employee {
    private String username;
    private int storeId;

    public Manager(int id, LocalDateTime startdate, LocalDateTime enddate, String username, int storeId) {
        super(id, startdate, enddate);
        this.username = username;
        this.storeId = storeId;
    }

    public String getUsername() {
        return username;
    }

    public int getStoreId() {
        return storeId;
    }
}
