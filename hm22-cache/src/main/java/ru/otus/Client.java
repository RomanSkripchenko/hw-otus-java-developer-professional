package ru.otus;

import ru.otus.jdbc.mapper.Id;

public class Client {
    @Id
    private Long id;
    private String name;

    public Client(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}