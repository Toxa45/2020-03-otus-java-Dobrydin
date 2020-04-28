package ru.otus;

import com.google.common.base.Joiner;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class App {
    public static void main(String[] args) {
        int min = 0;
        int max = 200;

        //заполнение листа
        List<Integer> diYarrayList = new DIYarrayList<>(max);
        for (int i = min; i < max; i++) {
            diYarrayList.add(i);
        }
        System.out.println("DIYarrayList - ");
        System.out.println(Joiner.on(", ").join(diYarrayList));

        //сортировка листа
        //сортируем - сначала четные, потом не четные
        Collections.sort(diYarrayList, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                    return integer%2==0 && t1%2==1?-1:(integer%2==1 && t1%2==0?1: Integer.compare(integer,t1));
            }
        });
        System.out.println("Отсортированный  DIYarrayList- ");
        System.out.println(Joiner.on(", ").join(diYarrayList));

        //заполнение листа чеоез addAll
        List<Integer> diYarrayListNew = new DIYarrayList<>(diYarrayList);
        diYarrayListNew.add(-100);
        Integer[] integers = diYarrayList.toArray(new Integer[0]);
        Collections.addAll(diYarrayListNew, integers );
        System.out.println("DIYarrayList после addAll - ");
        System.out.println(Joiner.on(", ").join(diYarrayListNew));

        //копирование листа
        //Сначала создадим копию листа через конструктор, отсортируем ее по другому правилу и применим Collections.copy
        List<Integer> diYarrayListCopy = new DIYarrayList<>(diYarrayList);
        //сортируем - сначала не четные, потом четные
        Collections.sort(diYarrayListCopy, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return integer%2==0 && t1%2==1?1:(integer%2==1 && t1%2==0?-1: Integer.compare(integer,t1));
            }
        });

        System.out.println("Отсортированный  diYarrayListCopy- ");
        System.out.println(Joiner.on(", ").join(diYarrayListCopy));

        Collections.copy(diYarrayListCopy, diYarrayList );
        System.out.println("diYarrayListCopy после копирования - ");
        System.out.println(Joiner.on(", ").join(diYarrayListCopy));
//        diYarrayListCopy.contains(5);
    }
}
