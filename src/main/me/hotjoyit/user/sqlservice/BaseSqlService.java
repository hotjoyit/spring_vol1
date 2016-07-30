package me.hotjoyit.user.sqlservice;

import javax.annotation.PostConstruct;

/**
 * Created by hotjoyit on 2016-07-30
 */
public class BaseSqlService implements SqlService {
  private SqlRegistry sqlRegistry;
  private SqlReader sqlReader;

  public void setSqlRegistry(SqlRegistry sqlRegistry) {
    this.sqlRegistry = sqlRegistry;
  }

  public void setSqlReader(SqlReader sqlReader) {
    this.sqlReader = sqlReader;
  }

  @PostConstruct
  public void loadSql() {
    sqlReader.read(sqlRegistry);
  }

  @Override
  public String getSql(String key) throws SqlRetrievalFailureException {
    try {
      return sqlRegistry.findSql(key);
    } catch (SqlNotFoundException e) {
      throw new SqlRetrievalFailureException(e.getMessage(), e);
    }
  }
}
