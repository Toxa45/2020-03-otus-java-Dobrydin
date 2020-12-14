package ru.otus.gsom.converters;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import javax.json.JsonValue;
import ru.otus.gsom.MyGson;

public class ConverterBase<T> implements Converter<T> {
  private final Predicate<Class<?>> isSuitable;
  private final BiFunction<T, MyGson,JsonValue> toJson;
  private final Class<T> tClass;

  public ConverterBase(Predicate<Class<?>> isSuitable,
      BiFunction<T,MyGson,JsonValue>  toJson,
      Class<T> tClass) {
    this.isSuitable = isSuitable;
    this.toJson = toJson;
    this.tClass = tClass;
  }

  public ConverterBase(BiFunction<T,MyGson,JsonValue> toJson,
      Class<T> tClass) {
    this.isSuitable = this::accept;
    this.toJson = toJson;
    this.tClass = tClass;
  }

  @Override
  public boolean isSuitable(Class<?> clazz) {
    return isSuitable.test(clazz);
  }

  @Override
  public JsonValue toJson(T object, MyGson myGson) {
    return toJson.apply(object, myGson);
  }

  private boolean accept(Class<?> clazz) {
    return this.tClass.isAssignableFrom(clazz);
  }

}
