package sample.model;

import java.time.LocalDateTime;

public class SalesAssociate extends Employee {

    public SalesAssociate(int id, String name, LocalDateTime startdate, LocalDateTime enddate) {
        super(id, name, startdate, enddate);
    }

    public void terminate() {
    }
}
