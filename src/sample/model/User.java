package sample.model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class User extends Person {

    public User(int id, String name, LocalDateTime createDate, String createdBy,
                LocalDateTime lastUpdate, String lastUpdatedBy) {
        super(id, name, createDate, createdBy, lastUpdate, lastUpdatedBy);
    }
}
