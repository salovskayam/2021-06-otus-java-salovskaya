package ru.otus.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.memento.DateTimeProvider;
import ru.otus.model.Message;


public class ProcessorThrowExceptionEvenSecond implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ProcessorThrowExceptionEvenSecond.class);

    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowExceptionEvenSecond(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        int sec = dateTimeProvider.getDate().getSecond();
        logger.info("Текущая секунда {}", sec);
        if (sec % 2 == 0) {
            throw new EvenSecondRuntimeException(
                String.format("Текущая секунда четная %s. Бросаем исключение.", sec));
        }
        return message;
    }

    private static class EvenSecondRuntimeException extends RuntimeException {
        public EvenSecondRuntimeException(String message) {
            super(message);
        }
    }
}
