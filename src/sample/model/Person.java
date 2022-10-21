package sample.model;

import java.time.ZonedDateTime;

abstract public class Person {
    private int id;
    private String name;
    private ZonedDateTime createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdatedBy;

    public Person(int id, String name, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate, String lastUpdatedBy) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastUpdate(ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }
}