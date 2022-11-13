package sample.model;

import java.time.LocalDateTime;

/**
 * Models the user who is loggin into the application.
 *
 * This class is used across the application for updating
 * records in the database according to the currently logged-in
 * user.
 *
 * @author Jose Marquez
 */
public class User extends Person {
    private LocalDateTime logInDateTime = null;

    /**
     * Constructor for the <code>User</code> class.
     *
     * @param id the integer id of the intended user
     * @param name the string name of the intended user
     * @param createDate the date the user was created in the database
     * @param createdBy the original creator of the user
     * @param lastUpdate the last user to update this user
     * @param lastUpdatedBy the last date/time the user was updated
     */
    public User(int id, String name, LocalDateTime createDate, String createdBy,
                LocalDateTime lastUpdate, String lastUpdatedBy) {
        super(id, name, createDate, createdBy, lastUpdate, lastUpdatedBy);
    }

    /**
     * Gets the login date/time of the current user.
     *
     * The <code>logInDateTime</code> will remain the same for the duration of the session.
     *
     * @return a <code>LocalDateTime</code> object representing the user's login date/time
     */
    public LocalDateTime getLogInDateTime() {
        return logInDateTime;
    }

    /**
     * Sets the time of initial log in for the current user when the application is first started.
     *
     * User lasts for the duration of the session.
     */
    public void setLogInDateTime() {
        logInDateTime = LocalDateTime.now();
    }
}
