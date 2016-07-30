package me.hotjoyit.user.sqlservice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hotjoyit on 2016-07-30
 */
public class HashMapSqlRegistry implements SqlRegistry {
  private Map<String, String> sqlMap = new HashMap<>();

  public void setSqlMap(Map<String, String> sqlMap) {
    this.sqlMap = sqlMap;
  }

  @Override
  public void registerSql(String key, String sql) {
    sqlMap.put(key, sql);
  }

  @Override
  public String findSql(String key) throws SqlNotFoundException {
    String sql = sqlMap.get(key);
    if (sql == null) {
      throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다");
    }
    return sql;
  }
}
