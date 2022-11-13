package sample.model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * An abstract class generally used to model people associated with
 * the company appointments.
 *
 * @author Jose Marquez
 */
abstract public class Person {
    private int id;
    private String name;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;

    /**
     * Constructor for the Person class when only an id and name are necessary.
     *
     * @param id the integer id of the intended person
     * @param name the string name of the intended person
     */
    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * General purpose constructor for the Person class.
     *
     * @param id the numeric id of the intended person
     * @param name the string name of the intended person
     * @param createDate the date the person being modeled was created in the database
     * @param createdBy the user the person was created by
     * @param lastUpdate the last date/time this person was updated
     * @param lastUpdatedBy the last user to update this person
     */
    public Person(int id, String name, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy) {
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}