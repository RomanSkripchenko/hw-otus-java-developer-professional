package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.ProcessorConcatFields;
import ru.otus.processor.ProcessorExceptionOnEvenSecond;
import ru.otus.processor.ProcessorSwapFields11And12;
import ru.otus.processor.homework.DateTimeProvider;
import ru.otus.processor.homework.LocalDateTimeProvider;

import java.util.List;

public class HomeWork {

    public static void main(String[] args) {
        DateTimeProvider dateTimeProvider = new LocalDateTimeProvider();
        var processors = List.of(
                new ProcessorSwapFields11And12(),
                new LoggerProcessor(new ProcessorConcatFields()),
                new ProcessorExceptionOnEvenSecond(dateTimeProvider)
        );

        var complexProcessor = new ComplexProcessor(processors, ex -> {
            System.err.println("Handled exception: " + ex.getMessage());
        });

        var listenerPrinter = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field11("value11")
                .field12("value12")
                .build();

        try {
            var result = complexProcessor.handle(message);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.err.println("Error during processing: " + e.getMessage());
        }

        // Пример использования HistoryListener
        var historyMessage = historyListener.findMessageById(1L);
        historyMessage.ifPresent(System.out::println);

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(historyListener);
    }
}
