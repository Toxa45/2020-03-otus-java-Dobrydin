package ru.otus.processor.homework;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

@DisplayName("Процессор, который поменяет местами значения field11 и field12")
class ProcessorExchangeField11AndField12Test {
  private Message message;

  @BeforeEach
  void setUp(){
    message = new Message.Builder(1L)
        .field1("field1")
        .field2("field2")
        .field3("field3")
        .field6("field6")
        .field11("field11")
        .field12("field12")
        .build();
  }


  @Test
  @DisplayName("Тестируемый процессор который меняет местами значения field11 и field12")
  void handleProcessorsTest() {

    Processor processorExchangeField11AndField12 = new ProcessorExchangeField11AndField12();
    Message messageNew = processorExchangeField11AndField12.process(this.message);

    assertThat(messageNew.getField12()).isEqualTo(this.message.getField11());
    assertThat(messageNew.getField11()).isEqualTo(this.message.getField12());
  }

}
