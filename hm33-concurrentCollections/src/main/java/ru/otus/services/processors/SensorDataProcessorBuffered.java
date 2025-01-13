package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> buffer;

    // Синхронизация на уровне объекта для управления flush
    private final Object flushLock = new Object();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.buffer = new PriorityBlockingQueue<>(bufferSize,
                Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {
        buffer.add(data);
        if (buffer.size() >= bufferSize) {
            flush();
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }

    protected void flush() {
        synchronized (flushLock) {
            if (buffer.isEmpty()) {
                return;
            }

            try {
                var flushedData = new ArrayList<SensorData>();
                buffer.drainTo(flushedData);
                writer.writeBufferedData(flushedData);
            } catch (Exception e) {
                log.error("Ошибка в процессе записи буфера", e);
            }
        }
    }
}
