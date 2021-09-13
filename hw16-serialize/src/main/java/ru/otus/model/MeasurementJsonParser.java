package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MeasurementJsonParser {
    @JsonCreator
    protected MeasurementJsonParser(@JsonProperty("name") String name, @JsonProperty("value") double value) {
    }
}
