package ru.otus.crm.model;

import jakarta.persistence.*;

@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)

    private Client client;

    public Phone() {}

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{id=" + id + ", number='" + number + "'}";
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
