package sample.model;

import java.time.LocalDateTime;

abstract public class Employee {
    private int id;
    private LocalDateTime startdate;
    private LocalDateTime enddate;

    public Employee(int id, LocalDateTime startdate, LocalDateTime enddate) {
        this.id = id;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public LocalDateTime getStartdate() {
        return startdate;
    }

    public LocalDateTime getEnddate() {
        return enddate;
    }
}
