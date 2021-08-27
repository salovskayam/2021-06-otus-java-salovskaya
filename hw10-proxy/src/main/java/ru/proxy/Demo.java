package ru.proxy;

public class Demo {

    public static void main(String[] args) {
        new Demo().action();
    }

    public void action() {
        TestLoggingProxy.createTestLoggingProxy().calculation();
        TestLoggingProxy.createTestLoggingProxy().calculation(6);
        TestLoggingProxy.createTestLoggingProxy().calculation(6, 10);
        TestLoggingProxy.createTestLoggingProxy().calculation(6, 11, "hello");
        TestLoggingProxy.createTestLoggingProxy().calculation("hello", "world");
        TestLoggingProxy.createTestLoggingProxy().calculation(100_000);
    }
}