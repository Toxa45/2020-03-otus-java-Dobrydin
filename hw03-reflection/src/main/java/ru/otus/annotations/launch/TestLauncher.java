package ru.otus.annotations.launch;

import ru.otus.annotations.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestLauncher {
    private static final String TEST_ANNOTATION = "Test";
    private static final String BEFORE_ANNOTATION = "Before";
    private static final String AFTER_ANNOTATION = "After";
    private int allTests = 0;
    private int completedTests = 0;
    public TestLauncher() {
    }
    public void run(String classNAme) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> clazz = Class.forName(classNAme);
        Method[] methods = clazz.getDeclaredMethods();
        Method[]  testAnnotationMethodsArray = getMethodsWithAnnotations(methods, Test.class);
        Method[]  afterAnnotationMethodsArray = getMethodsWithAnnotations(methods, After.class);
        Method[]  beforeAnnotationMethodsArray = getMethodsWithAnnotations(methods, Before.class);
        allTests = testAnnotationMethodsArray.length;

        invokeTestMethods(beforeAnnotationMethodsArray,testAnnotationMethodsArray,afterAnnotationMethodsArray, clazz.getConstructor());
        printStatistic(allTests, completedTests, allTests-completedTests);
    }

    private static Method[] getMethodsWithAnnotations(Method[] methods,Class<? extends Annotation> annotationClass) {
        Method[]  testAnnotationMethodsArray = Arrays.stream(methods).filter(method->method.isAnnotationPresent(annotationClass)).toArray(Method[]::new);
        return testAnnotationMethodsArray;
    }

    private void invokeTestMethods(Method[]  beforeAnnotationMethodsArray, Method[] testAnnotationMethodsArray, Method[]  afterAnnotationMethodsArray , Constructor<?> constructor) {
        for (Method method: testAnnotationMethodsArray) {
            Object objectTest;
            try {
                objectTest = constructor.newInstance();
            } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
                e.printStackTrace();
                continue;
            }
            try {
                invokeMethods(beforeAnnotationMethodsArray,objectTest);
                invokeMethod(method,objectTest);
            } catch (InvocationTargetException|IllegalAccessException e) {
                e.printStackTrace();
                System.out.println("Error execute method: " + method.getName());
            }finally {
                try {
                    invokeMethods(afterAnnotationMethodsArray,objectTest);
                    completedTests++;
                } catch (InvocationTargetException | IllegalAccessException ex) {
                    ex.printStackTrace();
                    System.out.println("Error execute After method " + method.getName());
                }
            }
        }

    }

    private static void invokeMethods(Method[]  methodsArray , Object objectTest) throws InvocationTargetException, IllegalAccessException {
        for (Method method: methodsArray) {
            invokeMethod(method,objectTest);
        }
    }

    private static void invokeMethod(Method method, Object objectTest) throws InvocationTargetException, IllegalAccessException {
       // не требуется т.к. before, alert  и т.д. помечаются публичные поля
        // method.setAccessible(true);
        method.invoke(objectTest);
    }


    private void printStatistic(int allTests, int completedTests, int errorTests) {
        System.out.println(String.format("\n====================\nAll tests: %d\nCompleted Tests: %d\nError Tests: %d\n====================", allTests, completedTests, errorTests));
    }
}
