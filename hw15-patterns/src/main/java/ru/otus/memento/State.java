package ru.otus.memento;

import ru.otus.model.Message;

public class State {
    private final Message message;

    public State(Message message) {
        this.message = message;
    }

    // создаем новый объект на основе существующего
    State(State state) {
        this.message = new Message(state.getMessage());
    }

    public Message getMessage() {
        return message;
    }
}
