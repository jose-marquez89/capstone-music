package sample.model;

import java.time.ZonedDateTime;

public class User extends Person {
    private String password;
    private static int passwordRetrievalsLeft = 1;

    public User(int id, String name, ZonedDateTime createDate, String createdBy,
                ZonedDateTime lastUpdate, String lastUpdatedBy, String password) {
        super(id, name, createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.password = password;
    }

    public String getPassword() {
        // not great logic
        if (passwordRetrievalsLeft-- > 0) {
            return password;
        }
        return "";
    }
}
