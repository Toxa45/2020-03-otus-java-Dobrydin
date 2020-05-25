package ru.otus;

import ru.otus.annotations.launch.TestLauncher;

import java.lang.reflect.InvocationTargetException;

public class App {
   public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException {
      TestLauncher testLauncher = new TestLauncher();
      testLauncher.run("ru.otus.TestClass");
   }
}
