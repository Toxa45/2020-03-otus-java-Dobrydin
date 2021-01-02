package ru.otus.gsom.converters;

import java.util.LinkedList;
import java.util.List;

public class ConvertersFactory {
  public static final List<Converter<?>> poolConverters(){
    final List<Converter<?>> converters = new LinkedList<>();
    converters.add(Converters.COLLECTION_CONVERTER);
    converters.add(Converters.ARRAY_CONVERTER);
    converters.add(Converters.BOOLEAN_CONVERTER);
    converters.add(Converters.CHARACTER_CONVERTER);
    converters.add(Converters.MAP_CONVERTER);
    converters.add(Converters.NUMBER_CONVERTER);
    converters.add(Converters.STRING_CONVERTER);
    converters.add(Converters.OBJECT_CLASS_CONVERTER);
    converters.add(Converters.SUPER_OBJECT_CLASS_CONVERTER);
    return converters;
  }
}
