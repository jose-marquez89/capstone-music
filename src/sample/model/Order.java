package sample.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private LocalDateTime createdAt;
    
    public Order(int id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
