package sample.model;

import java.time.LocalDateTime;

abstract public class Employee extends Person {
    private int id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Employee(int id, String name, LocalDateTime startDate, LocalDateTime endDate) {
        super(name);
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
