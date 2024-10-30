package ru.calculator;

public class Data {
    private final int value;  // Заменили Integer на int

    public Data(int value) {  // Соответственно, конструктор тоже принимает int
        this.value = value;
    }

    public int getValue() {   // Возвращаем int вместо Integer
        return value;
    }
}
