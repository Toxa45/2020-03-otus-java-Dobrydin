package ru.otus.jdbc.exception;

public class MapperException extends RuntimeException {

  public MapperException(Exception ex) {
    super(ex);
  }

  public MapperException(String msg) {
    super(msg);
  }

  public MapperException(String msg, ReflectiveOperationException e) {
      super(msg,e);
  }
}
