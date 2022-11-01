package sample.model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Customer extends Person {
    private String address;
    private String postalCode;
    private String phone;
    private String division;
    private String country;

    public Customer(int id, String name, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate,
                    String lastUpdatedBy, String address, String postalCode, String phone, String division, String country) {
        super(id, name, createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
        this.country = country;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
}
