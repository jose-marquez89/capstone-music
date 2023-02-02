package sample.model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * An abstract class generally used to model people associated with
 * company actions.
 *
 * @author Jose Marquez
 */
abstract public class Person {
    private String name;

    /**
     * Constructor for the Person class provides a simple base class for all system actors.
     *
     * @param name the string name of the intended person
     */
    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}