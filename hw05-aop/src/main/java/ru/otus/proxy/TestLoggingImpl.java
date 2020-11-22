package ru.otus.proxy;

import ru.otus.annotations.Log;

public class TestLoggingImpl implements TestLogging {

    @Log
    @Override
    public void secureAccess(String param, int param2) {
        System.out.println(String.format("Logging method secureAccess, param: %s, %d" ,param,param2));
    }

    @Log
    @Override
    public void secureAccess() {
        System.out.println("Logging method secureAccess not param");
    }

    @Override
    public void secureAccess(int param, String param2) {
        System.out.println(String.format("NOT Logging method secureAccess, param:%d, %s" ,param,param2));
    }


    @Override
    public String toString() {
        return "MyClassImpl{}";
    }

}
