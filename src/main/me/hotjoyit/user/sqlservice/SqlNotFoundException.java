package me.hotjoyit.user.sqlservice;

/**
 * Created by hotjoyit on 2016-07-30
 */
public class SqlNotFoundException extends RuntimeException {
  SqlNotFoundException(String message) {
    super(message);
  }

}
