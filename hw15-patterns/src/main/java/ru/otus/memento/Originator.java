package ru.otus.memento;

import java.util.*;

public class Originator {
    private final Map<Long, Memento> map = new HashMap<>();

    private final DateTimeProvider dateTimeProvider;

    public Originator(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    public void saveState(State state) {
        map.put(state.getMessage().getId(), new Memento(state, dateTimeProvider.getDate()));
    }

    public Optional<State> getState(Long id) {
        return Optional.ofNullable(map.get(id)).map(Memento::state);
    }
}
