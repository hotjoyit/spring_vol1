package me.hotjoyit.user.sqlservice;

/**
 * Created by hotjoyit on 2016-07-24
 */
public class SqlRetrievalFailureException extends RuntimeException {
  public SqlRetrievalFailureException(String message) {
    super(message);
  }

  public SqlRetrievalFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
