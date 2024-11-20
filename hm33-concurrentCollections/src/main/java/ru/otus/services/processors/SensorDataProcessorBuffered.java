package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final List<SensorData> buffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.buffer = new ArrayList<>();
    }

    @Override
    public synchronized void process(SensorData data) {
        buffer.add(data);
        if (buffer.size() >= bufferSize) {
            flush();
        }
    }

    @Override
    public synchronized void onProcessingEnd() {
        flush();
    }

    protected synchronized void flush() {
        if (buffer.isEmpty()) {
            return;
        }

        try {
            List<SensorData> sortedBuffer = new ArrayList<>(buffer);
            sortedBuffer.sort(Comparator.comparing(SensorData::getMeasurementTime));
            writer.writeBufferedData(sortedBuffer);
            buffer.clear();
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }
}
