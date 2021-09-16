package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementJsonParser;

import java.io.*;
import java.util.List;

public class FileLoader implements Loader {
    private final String fileName;
    private final ObjectMapper mapper = new ObjectMapper();

    public FileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        mapper.addMixIn(Measurement.class, MeasurementJsonParser.class);

        try {
            return mapper.readValue(
                    FileLoader.class.getClassLoader().getResourceAsStream(fileName),
                    new TypeReference<>() {
                    });
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
