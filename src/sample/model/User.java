package sample.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * Models the user who is loggin into the application.
 *
 * This class is used across the application for updating
 * records in the database according to the currently logged in
 * user.
 *
 * @author Jose Marquez
 */
public class User extends Person {
    private LocalDateTime logInDateTime = null;

    public User(int id, String name, LocalDateTime createDate, String createdBy,
                LocalDateTime lastUpdate, String lastUpdatedBy) {
        super(id, name, createDate, createdBy, lastUpdate, lastUpdatedBy);
    }

    public LocalDateTime getLogInDateTime() {
        return logInDateTime;
    }

    public void setLogInTime() {
        logInDateTime = LocalDateTime.now();
    }
}
