package sample.utility;

import java.time.LocalDateTime;

public class ReportRow {
    String name, store, city, state;
    LocalDateTime createdAt;
    double salePrice;

    public ReportRow(String name, String store, String city, String state, LocalDateTime createdAt, double salePrice) {
        this.name = name;
        this.store = store;
        this.city = city;
        this.state = state;
        this.createdAt = createdAt;
        this.salePrice = salePrice;
    }

    public String getName() {
        return name;
    }

    public String getStore() {
        return store;
    }

    public String getCity() {
        return city;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getState() {
        return state;
    }

    public double getSalePrice() {
        return salePrice;
    }
}
