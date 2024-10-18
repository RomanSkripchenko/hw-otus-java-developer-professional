package ru.otus.dataprocessor;

public class FileProcessException extends RuntimeException {
    // Конструктор, принимающий исключение
    public FileProcessException(Exception ex) {
        super(ex);
    }

    // Конструктор, принимающий сообщение
    public FileProcessException(String msg) {
        super(msg);
    }

    // Новый конструктор, принимающий сообщение и исключение
    public FileProcessException(String msg, Exception ex) {
        super(msg, ex);
    }
}
