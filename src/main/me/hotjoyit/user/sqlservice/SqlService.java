package me.hotjoyit.user.sqlservice;

/**
 * Created by hotjoyit on 2016-07-24
 */
public interface SqlService {
  String getSql(String key) throws SqlRetrievalFailureException;

}
