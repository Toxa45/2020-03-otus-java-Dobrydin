package ru.otus;

/**
 * @author dobrydin
 * @since 07.02.2021
 */
public class NotInterestingException extends RuntimeException {

  NotInterestingException(InterruptedException ex) {
    super(ex);
  }
}
