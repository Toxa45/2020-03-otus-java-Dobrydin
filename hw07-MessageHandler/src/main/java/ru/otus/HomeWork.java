package ru.otus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinter;
import ru.otus.listener.homework.ListenerHistory;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.ProcessorConcatFields;
import ru.otus.processor.ProcessorUpperField10;
import ru.otus.processor.homework.ProcessorExchangeField11AndField12;
import ru.otus.processor.homework.ProcessorThrowExceptionAnEvenSecond;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

  public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */
    var processors = List.of(new ProcessorConcatFields(),
        new ProcessorExchangeField11AndField12(),
        new ProcessorThrowExceptionAnEvenSecond(),
        new LoggerProcessor(new ProcessorUpperField10())
    );

    var complexProcessor = new ComplexProcessor(processors, (ex) -> { System.out.println("Ошибка!!! - "+ex.getMessage());
    });
    var listenerPrinter = new ListenerPrinter();
    var listenerHistory = new ListenerHistory();
    complexProcessor.addListener(listenerPrinter);
    complexProcessor.addListener(listenerHistory);

    List<String> data = new ArrayList(Arrays.asList("data1", "data2"));
    ObjectForMessage objectForMessage = new ObjectForMessage();
    objectForMessage.setData(data);

    var message = new Message.Builder(1L)
        .field1("field1")
        .field2("field2")
        .field3("field3")
        .field6("field6")
        .field10("field10")
        .field11("field11")
        .field12("field12")
        .field13(objectForMessage)
        .build();

    var result = complexProcessor.handle(message);
    System.out.println("\nresult:" + result+"\n");

    data.remove(0);
    data.add("data1Update");
    result = complexProcessor.handle(result);
    System.out.println("\nresult new:" + result+"\n");

    System.out.println("\nhistory:");
    listenerHistory.printHistory();
    complexProcessor.removeListener(listenerPrinter);
    complexProcessor.removeListener(listenerHistory);
  }
}
