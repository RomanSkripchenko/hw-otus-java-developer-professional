package app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
public class Address {
    @Id
    private Long id;

    private String street;

    @Column("client_id")
    private Long clientId; // Внешний ключ для связи с клиентом

    public Address() {}

    public Address(Long id, String street, Long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    // Геттеры и сеттеры
    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
