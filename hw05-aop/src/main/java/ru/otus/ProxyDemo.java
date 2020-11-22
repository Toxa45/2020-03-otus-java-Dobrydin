package ru.otus;

import ru.otus.proxy.Ioc;
import ru.otus.proxy.TestLoggingImpl;
import ru.otus.proxy.TestLogging;
import ru.otus.proxy.TestLoggingImpl2;

public class ProxyDemo {
    public static void main(String[] args) {
        //тут есть что логгировать
        System.out.println("\nимплиментация в которой есть логгирование");
        TestLogging testLogging =new TestLoggingImpl();
        TestLogging testLoggingproxy = Ioc.createProxyClass(testLogging);
        testLoggingproxy.secureAccess("First Param",2);
        testLoggingproxy.secureAccess();
        testLoggingproxy.secureAccess(1,"Second Param");


        System.out.println("\nимплиментация без логгирования");
        //а тут нет методов для логгирования
        TestLogging testLogging2 =new TestLoggingImpl2();
        TestLogging testLoggingproxy2 = Ioc.createProxyClass(testLogging2);
        testLoggingproxy2.secureAccess("First Param version 2",666);
        testLoggingproxy2.secureAccess();
        testLoggingproxy2.secureAccess(666,"Second Param version 2");
    }
}



