package sample.model;

import java.time.LocalDateTime;

/**
 * A class used to display existing customers across the application.
 *
 * @author Jose Marquez
 */
public class Customer extends Person {
    int divisionId;
    int countryId;
    private String address;
    private String postalCode;
    private String phone;
    private String division;
    private String country;

    /**
     * Constructor for the <code>Customer</code> class
     *
     * @param id the <code>customer_id</code> field from the <code>customers</code> table in database
     * @param divisionId the <code>division_id</code> field from the <code>customers</code> table in database
     * @param countryId the <code>country_id</code> field from the <code>customers</code> table in database
     * @param name the <code>name</code> field from the <code>customers</code> table in database
     * @param createDate the <code>create_date</code> field from the <code>customers</code> table in database
     * @param createdBy the <code>created_by</code> field from the <code>customers</code> table in database
     * @param lastUpdate the <code>last_updated</code> field from the <code>customers</code> table in database
     * @param lastUpdatedBy the <code>last_updated_by</code> field from the <code>customers</code> table in database
     * @param address the <code>address</code> field from the <code>customers</code> table in database
     * @param postalCode the <code>postal_code</code> field from the <code>customers</code> table in database
     * @param phone the <code>phone</code> field from the <code>customers</code> table in database
     * @param division the <code>division_id</code> field from the <code>first_level_divisions</code> table in database
     * @param country the <code>country_name</code> field from the <code>countries</code> table in database
     */
    public Customer(int id, int divisionId, int countryId, String name, LocalDateTime createDate,
                    String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, String address,
                    String postalCode, String phone, String division, String country) {
        super(id, name, createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
        this.country = country;
        this.divisionId = divisionId;
        this.countryId = countryId;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getDivision() {
        return division;
    }

    public int getCountryId() {
        return countryId;
    }

    public int getDivisionId() {
        return divisionId;
    }
}
