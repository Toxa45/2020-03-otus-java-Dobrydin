package ru.otus.listener.homework;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.ListenerHistory.HistoryMessage;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.ProcessorConcatFields;
import ru.otus.processor.ProcessorUpperField10;

@DisplayName("Listener для ведения истории: старое сообщение - новое")
class ListenerHistoryTest {

  @Test
  @DisplayName("Listener должен вести историю которая не портится, изменяем поле Field13")
  void onUpdated() {
    var listenerHistory = new ListenerHistory();

    var processors = List.of(new ProcessorConcatFields(),
        new LoggerProcessor(new ProcessorUpperField10()));

    var complexProcessor = new ComplexProcessor(processors, (ex) -> {});
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
        .field13(objectForMessage)
        .build();

    var result = complexProcessor.handle(message);
    System.out.println("result:" + result);

    Queue<HistoryMessage> historyMessage = listenerHistory.getHistoryMessage();

    HistoryMessage element = historyMessage.element();

    historyMessage.clear();
    assertThat(element.getOldMsg().getField13()).isEqualTo(message.getField13());
    assertThat(element.getNewMsg().getField13()).isEqualTo(result.getField13());

    data.add("data1Update");
    data.remove(0);
    assertThat(element.getOldMsg().getField13()).isNotEqualTo(message.getField13());
    assertThat(element.getNewMsg().getField13()).isNotEqualTo(result.getField13());

    List<String> dataNEW = new ArrayList(Arrays.asList("data1NEW", "data2NEW"));
    objectForMessage.setData(dataNEW);

    assertThat(element.getOldMsg().getField13()).isNotEqualTo(message.getField13());
    assertThat(element.getNewMsg().getField13()).isNotEqualTo(result.getField13());
    complexProcessor.removeListener(listenerHistory);
  }
}
