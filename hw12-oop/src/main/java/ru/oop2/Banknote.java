package ru.oop2;

import java.util.Objects;

public class Banknote {

    private final int value;

    public Banknote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banknote banknote = (Banknote) o;
        return value == banknote.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
