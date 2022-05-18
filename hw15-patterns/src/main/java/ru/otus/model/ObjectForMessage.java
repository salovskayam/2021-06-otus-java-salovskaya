package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage cloneObjectForMessage() {
        ObjectForMessage result = new ObjectForMessage();
        if (data != null) {
            List<String> copyData = new ArrayList<>(getData());
            result.setData(copyData);
        }
        return result;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }
}
