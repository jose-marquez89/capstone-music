package sample.model;

import java.time.LocalDateTime;

abstract public class Employee extends Person {
    private int id;
    private LocalDateTime startdate;
    private LocalDateTime enddate;

    public Employee(int id, String name, LocalDateTime startdate, LocalDateTime enddate) {
        super(name);
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
