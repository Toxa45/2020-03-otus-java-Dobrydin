package ru.otus.hibernate.exception;

public class DaoException extends RuntimeException {

  public DaoException(Exception ex) {
    super(ex);
  }
}
