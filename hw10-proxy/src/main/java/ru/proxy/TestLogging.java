package ru.proxy;

import ru.proxy.Log;

public class TestLogging implements Calculation {
    @Log
    public void calculation() {}

    @Log
    public void calculation(int param) {
    }

    @Log
    public void calculation(int param1, int param2) {
    }

    @Log
    public void calculation(int param1, int param2, String param3) {
    }

    public void calculation(String param4, String param5) {
        System.out.println("method without annotation");
    }

    @Log
    public String calculation(Long param) {
        return null;
    }
}