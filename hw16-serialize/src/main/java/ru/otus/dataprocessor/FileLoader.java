package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
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
        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(
                List.class, MeasurementJsonParser.class);

        try {
            return mapper.readValue(
                    FileLoader.class.getClassLoader().getResourceAsStream(fileName),
                    collectionType);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
