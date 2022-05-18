package ru.otus.memento;


import java.time.LocalDateTime;

public record Memento(State state, LocalDateTime executedAt) {
    // можно сохранить дополнительные метаданные
    public Memento(State state, LocalDateTime executedAt) {
        this.state = new State(state);
        this.executedAt = executedAt;
    }
}
