package app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
public class Phone {
    @Id
    private Long id;

    private String number;

    public Phone() {}

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
