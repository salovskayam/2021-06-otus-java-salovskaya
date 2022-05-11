package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.memento.Originator;
import ru.otus.memento.State;
import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Originator originator = new Originator(LocalDateTime::now);

    @Override
    public void onUpdated(Message msg) {
        State state = new State(msg);
        originator.saveState(state);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return originator.getState(id).map(State::getMessage);
    }
}
