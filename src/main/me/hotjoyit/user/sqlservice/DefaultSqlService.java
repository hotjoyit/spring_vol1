package me.hotjoyit.user.sqlservice;

/**
 * Created by hotjoyit on 2016-07-30
 */
public class DefaultSqlService extends BaseSqlService {
  public DefaultSqlService() {
    setSqlReader(new JaxbXmlSqlReader());
    setSqlRegistry(new HashMapSqlRegistry());
  }
}
