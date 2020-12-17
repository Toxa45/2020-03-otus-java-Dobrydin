package ru.otus.gsom;

import java.util.List;
import javax.json.JsonValue;
import javax.json.spi.JsonProvider;
import lombok.Getter;
import ru.otus.gsom.converters.Converter;
import ru.otus.gsom.converters.ConvertersFactory;

@Getter
public class MyGson {

  private final List<Converter<?>> converters;

  private final JsonProvider jsonProvider = JsonProvider.provider();

  public MyGson() {
    converters = ConvertersFactory.poolConverters();
  }

  public String toJson(Object object) {
    return toJsonValue(object).toString();
  }

  public JsonValue toJsonValue(Object object) {
    if (object == null) {
      return JsonValue.NULL;
    }
    if (object instanceof JsonValue) {
      return (JsonValue) object;
    }

    Class<?> clazz = object.getClass();
    Converter converter = findConverter(clazz);
    return converter.toJson(object, this);
  }


  private Converter findConverter(Class<?> clazz) {
    return converters.stream()
        .filter(a -> a.isSuitable(clazz))
        .findFirst()
        .orElseThrow(() -> new RuntimeException(
            clazz.toString()));
  }

}
