package me.hotjoyit.user.sqlservice;

import me.hotjoyit.user.dao.UserDao;
import me.hotjoyit.user.sqlservice.jaxb.SqlType;
import me.hotjoyit.user.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hotjoyit on 2016-07-30
 */
public class XmlSqlService implements SqlService {
  private Map<String, String> sqlMap = new HashMap<>();
  private String sqlmapFile;

  public void setSqlmapFile(String sqlmapFile) {
    this.sqlmapFile = sqlmapFile;
  }

  public XmlSqlService() {

  }

  @PostConstruct
  public void loadSql() {
    String contextPath = Sqlmap.class.getPackage().getName();
    try {
      JAXBContext context = JAXBContext.newInstance(contextPath);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
      Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);

      for (SqlType sql : sqlmap.getSql()) {
        sqlMap.put(sql.getKey(), sql.getValue());
      }
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getSql(String key) throws SqlRetrievalFailureException {
    String sql = sqlMap.get(key);
    if (sql == null) {
      throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다");
    }
    return sql;
  }
}
