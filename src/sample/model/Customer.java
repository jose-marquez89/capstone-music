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
