package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ProcessorExceptionOnEvenSecond implements Processor {

    @Override
    public Message process(Message message) {
        int second = LocalDateTime.now().getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException("Exception in even second: " + second);
        }
        return message;
    }
}
