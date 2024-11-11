package ru.otus.listener.homework;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.ProcessorExceptionOnEvenSecond;
import ru.otus.processor.homework.DateTimeProvider;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class ProcessorExceptionOnEvenSecondTest {

    @Test
    void shouldThrowExceptionOnEvenSecond() {
        DateTimeProvider dateTimeProvider = mock(DateTimeProvider.class);
        when(dateTimeProvider.getDateTime()).thenReturn(LocalDateTime.of(2023, 10, 15, 10, 0, 2)); // Even second

        Processor processor = new ProcessorExceptionOnEvenSecond(dateTimeProvider);

        assertThatThrownBy(() -> processor.process(new Message.Builder(1L).build()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Even second exception");
    }

    @Test
    void shouldNotThrowExceptionOnOddSecond() {
        DateTimeProvider dateTimeProvider = mock(DateTimeProvider.class);
        when(dateTimeProvider.getDateTime()).thenReturn(LocalDateTime.of(2023, 10, 15, 10, 0, 3)); // Odd second

        Processor processor = new ProcessorExceptionOnEvenSecond(dateTimeProvider);

        assertThatNoException().isThrownBy(() -> processor.process(new Message.Builder(1L).build()));
    }
}

