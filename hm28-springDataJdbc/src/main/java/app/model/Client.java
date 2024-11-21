package app.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Client {
    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "client_id")
    private List<Phone> phones = new ArrayList<>();

    private Address address;

    // Конструктор по умолчанию
    public Client() {}

    // Конструктор с параметрами
    public Client(Long id, String name, List<Phone> phones, Address address) {
        this.id = id;
        this.name = name;
        this.phones = phones;
        this.address = address;
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

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
