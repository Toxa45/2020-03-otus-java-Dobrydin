package ru.otus.processor.homework;

import java.time.LocalDateTime;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorThrowExceptionAnEvenSecond implements Processor {

    @Override
    public Message process(Message message) {
        LocalDateTime now = LocalDateTime.now();
        int second = now.getSecond();
        if(second % 2 == 0 )
            throw new RuntimeException("Четная секунда, текущее время: "+now);
        System.out.println("log processing message:" + message);
        return message;
    }
}
