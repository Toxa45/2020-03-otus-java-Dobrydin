package ru.otus.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class SuperAnyObject {
  private int fieldIntSuper = 666;
  private double fieldDoubleSuper = 666.666;
  private Integer fieldIntegerWrapSuper = 666_666;
  private Double fieldDoubleWrapSuper = 666_666.666;
  private String fieldAtringSuper = "Super New Year";
  private List<Integer> integerListSuper = Arrays.asList(new Integer[]{16, 26, 36});
  private transient List<Integer> integerListTransientSuper = Arrays.asList(new Integer[]{46, 56, 66});
  private int[] masIntSuper =new int[]{116, 126, 136};
  private Map<String,Double> doubleMapSuper = Map.of("key1Super", 1.56,"key2Super",2.56);
}
