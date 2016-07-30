package me.hotjoyit.user.sqlservice;

/**
 * Created by hotjoyit on 2016-07-30
 */
public interface SqlRegistry {
  void registerSql(String key, String sql);
  String findSql(String key) throws SqlNotFoundException;
}
