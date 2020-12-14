package ru.otus.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor()
public class AnyObject extends SuperAnyObject{
  private static int fieldIntStatic = 100_000;
  private int fieldInt;
  private double fieldDouble;
  private Integer fieldIntegerWrap;
  private Double fieldDoubleWrap;
  private String fieldAtring;
  private List<Integer> integerList = Arrays.asList(new Integer[]{1, 2, 3});
  private transient List<Integer> integerListTransient = Arrays.asList(new Integer[]{4, 5, 6});
  private int[] masInt =new int[]{11, 12, 13};
  private Map<String,Double> doubleMap = Map.of("key1", 1.5,"key2",2.5);
  private SuperAnyObject SuperAnyObject = new SuperAnyObject();
  private LocalDate localDate = LocalDate.now();

  public AnyObject(int fieldInt, double fieldDouble, Integer fieldIntegerWrap,
      Double fieldDoubleWrap, String fieldAtring) {
    super();
    this.fieldInt = fieldInt;
    this.fieldDouble = fieldDouble;
    this.fieldIntegerWrap = fieldIntegerWrap;
    this.fieldDoubleWrap = fieldDoubleWrap;
    this.fieldAtring = fieldAtring;
  }
}
