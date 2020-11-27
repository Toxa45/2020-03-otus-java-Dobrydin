package ru.otus.proxy;


import ru.otus.annotations.Log;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Ioc {
    private Ioc() {
    }

    public static TestLogging createProxyClass(TestLogging testLogging) {
        InvocationHandler handler = new DemoInvocationHandler(testLogging);
        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLogging testLogging;
        private final Set<Method> testingMethods;

        DemoInvocationHandler(TestLogging testLogging) {
            this.testLogging = testLogging;
            testingMethods = Arrays.stream(testLogging.getClass().getMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class))
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (testingMethods.stream()
                    .anyMatch( testingM->
                            testingM.getName().equals(method.getName())
                            &&  Arrays.equals(testingM.getParameterTypes(), method.getParameterTypes())
                    )
            )
                System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args).replaceAll("[\\[\\]]", ""));
            return method.invoke(testLogging, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "TestLogging=" + testLogging +
                    '}';
        }
    }
}
