package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dobrydin
 * @since 07.02.2021
 */
public class ExecutorsPrinter {

  private static final Logger logger = LoggerFactory.getLogger(ExecutorsPrinter.class);
  private boolean isTh1 = true;
  private final MessageGenerate messageGenerate;

  public ExecutorsPrinter(MessageGenerate messageGenerate) {
    this.messageGenerate = messageGenerate;
  }

  public static void main(String[] args) {
    var executorsPrinter = new ExecutorsPrinter(new MessageGenerateInt());
    new Thread(() -> executorsPrinter.action(true)).start();
    new Thread(() -> executorsPrinter.action(false)).start();
  }

  private synchronized void action(boolean isThead1) {
    while (true) {
      try {
        while (isTh1 != isThead1) {
          this.wait();
        }
        printMessageAndTurnOver();
        sleep();
        notifyAll();
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        throw new NotInterestingException(ex);
      }
    }
  }

  private void printMessageAndTurnOver() {
    logger.info("Thead info {} - {}", Thread.currentThread().getName(),
        messageGenerate.messageNext());

    isTh1 = !isTh1;
  }


  private static void sleep() {
    try {
      Thread.sleep(1_000);
    } catch (InterruptedException e) {
      logger.error(e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

}
