package ru.otus;

public class Manager {
    @Id
    private Long no;
    private String name;

    public Manager(String name) {
        this.name = name;
    }

    public Long getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public void setNo(Long no) {
        this.no = no;
    }
}
