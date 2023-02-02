package sample.model;

import java.time.LocalDateTime;

public class SalesAssociate extends Employee {

    public SalesAssociate(int id, String name, LocalDateTime startDate, LocalDateTime endDate) {
        super(id, name, startDate, endDate);
    }

    public void terminate() {
    }
}
