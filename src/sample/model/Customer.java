package sample.model;

import java.time.ZonedDateTime;

public class Customer extends Person {
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;

    public Customer(int id, String name, ZonedDateTime createDate, String createdBy, ZonedDateTime lastUpdate,
                    String lastUpdatedBy, String address, String postalCode, String phone, int divisionId) {
        super(id, name, createDate, createdBy, lastUpdate, lastUpdatedBy);
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
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

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
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

    public int getDivisionId() {
        return divisionId;
    }
}
