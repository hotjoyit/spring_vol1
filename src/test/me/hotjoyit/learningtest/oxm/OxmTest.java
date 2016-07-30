package me.hotjoyit.learningtest.oxm;

import me.hotjoyit.user.sqlservice.jaxb.SqlType;
import me.hotjoyit.user.sqlservice.jaxb.Sqlmap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by hotjoyit on 2016-07-30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class OxmTest {
  @Autowired
  private Unmarshaller unmarshaller;

  @Test
  public void unmarshallSqlMap() throws IOException {
    Source xmlSource = new StreamSource(getClass().getResourceAsStream("sqlmap.xml"));
    Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(xmlSource);

    List<SqlType> sqlList = sqlmap.getSql();
    assertThat(sqlList.size(), is(3));
    assertThat(sqlList.get(0).getKey(), is("add"));
    assertThat(sqlList.get(2).getValue(), is("delete"));
  }
}
