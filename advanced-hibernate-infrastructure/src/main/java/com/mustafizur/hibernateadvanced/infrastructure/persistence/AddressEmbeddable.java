package com.mustafizur.hibernateadvanced.infrastructure.persistence;

import jakarta.persistence.Embeddable;

@Embeddable
public class AddressEmbeddable {
    private String street;
    private String city;
    private String postalCode;
    private String countryCode;

    protected AddressEmbeddable() {
    }

    public AddressEmbeddable(String street, String city, String postalCode, String countryCode) {
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.countryCode = countryCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
