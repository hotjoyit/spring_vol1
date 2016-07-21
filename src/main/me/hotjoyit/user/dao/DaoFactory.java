package me.hotjoyit.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * Created by hotjoyit on 2016-07-19
 */
@Configuration
public class DaoFactory {

  @Bean
  public UserDao userDao() {
    UserDaoJdbc userDao = new UserDaoJdbc();
    userDao.setDataSource(dataSource());
    return userDao;
  }

  @Bean
  public DataSource dataSource() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

    dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
    dataSource.setUrl("jdbc:mysql://localhost/toby_spring");
    dataSource.setUsername("hotjoyit");
    dataSource.setPassword("pwhotjoyit");

    return dataSource;
  }
}
