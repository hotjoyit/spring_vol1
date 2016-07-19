package me.hotjoyit.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by hotjoyit on 2016-07-19
 */
public class ZUserDao extends UserDao {

  public Connection getConnection() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    Connection c = DriverManager.getConnection("jdbc:mysql://localhost/toby_spring", "hotjoyit", "pwhotjoyit");
    return c;
  }
}
