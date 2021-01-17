package ru.otus.jdbc.exception;

public class MetadataException extends RuntimeException {

  public MetadataException(Exception ex) {
    super(ex);
  }

  public MetadataException(String msg) {
    super(msg);
  }

  public MetadataException(String msg, IllegalAccessException e) {
      super(msg,e);
  }
}
