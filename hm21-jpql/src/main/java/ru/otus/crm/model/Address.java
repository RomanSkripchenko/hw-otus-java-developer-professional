package ru.otus.crm.model;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;

    public Address() {}

    public Address(String street) {
        this.street = street;
    }


    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{id=" + id + ", street='" + street + "'}";
    }
}
