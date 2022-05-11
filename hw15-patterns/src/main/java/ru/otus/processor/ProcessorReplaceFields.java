package ru.otus.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Message;

public class ProcessorReplaceFields implements Processor {
    private static final Logger logger = LoggerFactory.getLogger(ProcessorReplaceFields.class);

    @Override
    public Message process(Message message) {
        logger.info("replace field11 and field12");
        return message.toBuilder()
                .field11(message.getField12())
                .field12(message.getField11())
                .build();
    }
}
