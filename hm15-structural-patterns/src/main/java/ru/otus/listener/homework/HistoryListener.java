package ru.otus.listener.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long, Message> history = new HashMap<>(); // Инициализация поля history

    @Override
    public void onUpdated(Message msg) {
        history.put(msg.getId(), msg.toBuilder().build()); // Сохраняем копию сообщения
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(history.get(id)); // Возвращаем сообщение по id
    }
}
