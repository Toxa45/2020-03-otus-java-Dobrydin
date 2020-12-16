package ru.otus.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectForMessage implements CloneableObject<ObjectForMessage> {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage copy() {
        ObjectForMessage objectForMessageClone = new ObjectForMessage();
        List<String> dataClone = Optional.ofNullable(data)
            .map(List::stream)
            .orElseGet(Stream::empty)
            .collect(Collectors.toList());
        objectForMessageClone.setData(dataClone);
        return objectForMessageClone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ObjectForMessage that = (ObjectForMessage) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
            "data=" + data +
            '}';
    }
}
