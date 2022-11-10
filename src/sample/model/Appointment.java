package sample.model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * This class creates Appointment objects that allow for appoinment display
 * across the application.
 *
 * Provides all the fields necessary to display and filter appointments in
 * tables across the application.
 *
 * @author Jose Marquez
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerId;
    private int userId;
    private int contactId;

    public Appointment(int id, String title, String description, String location, String contact,
                       String type, LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getContact() {
        return contact;
    }
    public int getContactId() {
        return contactId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getUserId() {
        return userId;
    }
}
