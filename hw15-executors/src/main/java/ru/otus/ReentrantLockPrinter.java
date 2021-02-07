package ru.otus;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dobrydin
 * @since 07.02.2021
 */
public class ReentrantLockPrinter {
  private static final Logger logger = LoggerFactory.getLogger(ReentrantLockPrinter.class);
  private boolean isTh1 = true;
  private final MessageGenerate messageGenerate;
  private final Lock lock = new ReentrantLock();

  public ReentrantLockPrinter(MessageGenerate messageGenerate) {
    this.messageGenerate = messageGenerate;
  }


  public static void main(String[] args) {
    new ReentrantLockPrinter(new MessageGenerateInt()).go();
  }


  private void go() {
    Condition condition = lock.newCondition();
    new Thread(() -> this.action(true,lock,condition)).start();
    new Thread(() -> this.action(false,lock,condition)).start();
  }


  private void action(boolean isThead1,Lock lock,Condition condition) {
    while (true){
      lock.lock();
      try {
        while (isTh1!=isThead1) {
            condition.await();
        }
        printMessageAndTurnOver();
        sleep();
        condition.signalAll();
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        throw new NotInterestingException(ex);
      }
      finally {
        lock.unlock();
      }
    }
  }

  private void printMessageAndTurnOver(){
    logger.info("Thead info {} - {}",Thread.currentThread().getName(), messageGenerate.messageNext());

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
