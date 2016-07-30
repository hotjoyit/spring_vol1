package me.hotjoyit.user.sqlservice;

import me.hotjoyit.user.dao.UserDao;
import me.hotjoyit.user.sqlservice.jaxb.SqlType;
import me.hotjoyit.user.sqlservice.jaxb.Sqlmap;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/**
 * Created by hotjoyit on 2016-07-30
 */
public class OxmSqlService implements SqlService {
  private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

  private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

  public void setSqlRegistry(SqlRegistry sqlRegistry) {
    this.sqlRegistry = sqlRegistry;
  }

  public void setUnmarshaller(Unmarshaller unmarshaller) {
    this.oxmSqlReader.setUnmarshaller(unmarshaller);
  }

  public void setSqlmapFile(String sqlmapFile) {
    this.oxmSqlReader.setSqlmapFile(sqlmapFile);
  }

  @PostConstruct
  public void loadSql() {
    this.oxmSqlReader.read(this.sqlRegistry);
  }

  @Override
  public String getSql(String key) throws SqlRetrievalFailureException {
    try {
      return sqlRegistry.findSql(key);
    } catch (SqlNotFoundException e) {
      throw new SqlRetrievalFailureException(e.getMessage(), e);
    }
  }

  private class OxmSqlReader implements SqlReader {
    private Unmarshaller unmarshaller;
    private final static String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
    private String sqlmapFile = DEFAULT_SQLMAP_FILE;

    public void setUnmarshaller(Unmarshaller unmarshaller){
      this.unmarshaller = unmarshaller;
    }

    public void setSqlmapFile(String sqlmapFile) {
      this.sqlmapFile = sqlmapFile;
    }

    @Override
    public void read(SqlRegistry sqlRegistry) {
      try {
        Source source = new StreamSource(UserDao.class.getResourceAsStream(this.sqlmapFile));
        Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(source);

        for (SqlType sql : sqlmap.getSql()) {
          sqlRegistry.registerSql(sql.getKey(), sql.getValue());
        }
      } catch (IOException e) {
        throw new IllegalArgumentException(this.sqlmapFile + "을 가져올 수 없습니다", e);
      }
    }
  }
}
