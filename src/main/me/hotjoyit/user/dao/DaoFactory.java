package me.hotjoyit.user.dao;

/**
 * Created by hotjoyit on 2016-07-19
 */
public class DaoFactory {

  public UserDao userDao() {
    ConnectionMaker connectionMaker = new ZConnectionMaker();
    UserDao userDao = new UserDao(connectionMaker);
    return userDao;
  }
}
