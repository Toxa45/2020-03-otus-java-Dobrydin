package ru.otus.processor.homework;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import ru.otus.model.Message;

@DisplayName("Процессор, который выбрасывает исключение только в четные секунды")
class ProcessorThrowExceptionAnEvenSecondTest {

  @Test
  @DisplayName("Тестируемый процессор должен выбрасывать исключение только в четные секунды")
  public void processorUnderTestShouldOnlyThrowAnExceptionInEvenSeconds() {
    String instantExpected = "2014-12-22T10:15:30Z";
    Clock clock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
    LocalDateTime fixedLocalDateEvenSecond = LocalDateTime.now(clock);

    LocalDateTime fixedLocalDateEvenSecondOptionTwo = LocalDateTime.of(2020, 12, 06, 10, 10, 10);

    LocalDateTime fixedLocalDateNotEvenSecond = LocalDateTime.of(2020, 12, 06, 10, 10, 11);

    try (MockedStatic mocked = mockStatic(LocalDateTime.class)) {
      mocked.when(LocalDateTime::now).thenReturn(fixedLocalDateEvenSecond);
      var message = new Message.Builder(1L).field7("field7").build();

      var processorThrowExceptionAnEvenSecond = new ProcessorThrowExceptionAnEvenSecond();

      assertThrows(RuntimeException.class,
          () -> processorThrowExceptionAnEvenSecond.process(message));

      mocked.when(LocalDateTime::now).thenReturn(fixedLocalDateEvenSecondOptionTwo);

      assertThrows(RuntimeException.class,
          () -> processorThrowExceptionAnEvenSecond.process(message));

      mocked.when(LocalDateTime::now).thenReturn(fixedLocalDateNotEvenSecond);

      assertThat(processorThrowExceptionAnEvenSecond.process(message)).isEqualTo(message);
    }
  }
}
