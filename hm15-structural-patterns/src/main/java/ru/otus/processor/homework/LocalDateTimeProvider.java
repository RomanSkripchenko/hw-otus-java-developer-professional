package ru.otus.processor.homework;

import java.time.LocalDateTime;

public class LocalDateTimeProvider implements DateTimeProvider {
    @Override
    public LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }
}

