package ru.otus.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MeasurementJsonParser extends Measurement{

    @JsonCreator
    public MeasurementJsonParser(@JsonProperty("name") String name, @JsonProperty("value") double value) {
        super(name, value);
    }

}
