package ru.otus;

import ru.otus.dataprocessor.*;
import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String inputFileName = "measurements.json"; // входной файл
        String outputFileName = "output.json"; // выходной файл

        Loader loader = new ResourcesFileLoader(inputFileName);
        List<Measurement> measurements = loader.load();

        Processor processor = new ProcessorAggregator();
        Map<String, Double> processedData = processor.process(measurements);

        Serializer serializer = new FileSerializer(outputFileName);
        serializer.serialize(processedData);

        System.out.println("Processing completed successfully!");
    }
}
