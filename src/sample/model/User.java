package sample.model;

import java.time.ZonedDateTime;

public class User extends Person {

    public User(int id, String name, ZonedDateTime createDate, String createdBy,
                ZonedDateTime lastUpdate, String lastUpdatedBy) {
        super(id, name, createDate, createdBy, lastUpdate, lastUpdatedBy);
    }
}
