package ru.otus.crm.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();

    // Конструктор по умолчанию
    public Client() {}

    // Конструктор с параметром имени
    public Client(String name) {
        this.name = name;
        this.address = null; // По умолчанию, null
        this.phones = new ArrayList<>(); // Пустой список телефонов
    }

    // Конструктор с параметрами id и name (для обновления)
    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Изменение конструктора с параметрами name, address и phones
    public Client(String name, Address address, List<Phone> phones) {
        this.name = name;
        this.address = address;
        this.phones = phones != null ? phones : new ArrayList<>(); // Проверка на null
    }

    // Обновление конструктора с параметрами id, name, address и phones
    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones != null ? phones : new ArrayList<>(); // Проверка на null
    }



    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public Client clone() {
        return new Client(this.name, this.address, new ArrayList<>(this.phones));
    }

    @Override
    public String toString() {
        return "Client{id=" + id + ", name='" + name + "', address=" + address + ", phones=" + phones + "}";
    }
}
