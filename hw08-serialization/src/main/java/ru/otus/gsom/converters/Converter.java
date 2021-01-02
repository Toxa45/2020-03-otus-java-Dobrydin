package ru.otus.gsom.converters;

import javax.json.JsonValue;
import ru.otus.gsom.MyGson;

public interface Converter<T> {

  boolean isSuitable(Class<?> clazz);

  JsonValue toJson(T object, MyGson myGson);
}
