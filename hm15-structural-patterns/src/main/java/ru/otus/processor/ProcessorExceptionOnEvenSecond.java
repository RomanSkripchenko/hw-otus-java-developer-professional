package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

import java.time.LocalDateTime;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.DateTimeProvider;

public class ProcessorExceptionOnEvenSecond implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorExceptionOnEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int second = dateTimeProvider.getDateTime().getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException("Even second exception");
        }
        return message;
    }
}

