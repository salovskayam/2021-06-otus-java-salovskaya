package ru.proxy;

public interface Calculation {
    void calculation();

    void calculation(int param);

    void calculation(int param1, int param2);

    void calculation(int param1, int param2, String param3);

    void calculation(String param4, String param5);

    String calculation(Long param);

}