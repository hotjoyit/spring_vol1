package me.hotjoyit.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hotjoyit on 2016-07-19
 */
@Configuration
public class DaoFactory {

  @Bean
  public UserDao userDao() {
    UserDao userDao = new UserDao(connectionMaker());
    return userDao;
  }

  @Bean
  public ConnectionMaker connectionMaker() {
    return new ZConnectionMaker();
  }
}
