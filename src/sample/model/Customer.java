package sample.model;

import java.time.LocalDateTime;

/**
 * A class used to display existing customers across the application.
 *
 * @author Jose Marquez
 */
public class Customer extends Person {
    private String phone;
    private String email;

    /**
     * Constructor for the <code>Customer</code> class
     *
     * @param phone the <code>phone</code> field from the <code>customers</code> table in database
     * @param email the customer's email in username@domain.com format
     */
    public Customer(int id, String name, String email, String phone) {
        super(id, name);
        this.phone = phone;
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
