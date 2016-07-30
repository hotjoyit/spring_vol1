package me.hotjoyit.user.sqlservice;

import me.hotjoyit.user.dao.UserDao;
import me.hotjoyit.user.sqlservice.jaxb.SqlType;
import me.hotjoyit.user.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * Created by hotjoyit on 2016-07-30
 */
public class JaxbXmlSqlReader implements SqlReader {
  private String sqlmapFile;

  public void setSqlmapFile(String sqlmapFile) {
    this.sqlmapFile = sqlmapFile;
  }

  @Override
  public void read(SqlRegistry sqlRegistry) {
    String contextPath = Sqlmap.class.getPackage().getName();
    try {
      JAXBContext context = JAXBContext.newInstance(contextPath);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      InputStream is = UserDao.class.getResourceAsStream(this.sqlmapFile);
      Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);

      for (SqlType sql : sqlmap.getSql()) {
        sqlRegistry.registerSql(sql.getKey(), sql.getValue());
      }
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }
}
