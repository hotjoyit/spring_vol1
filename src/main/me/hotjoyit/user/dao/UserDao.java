package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.User;

import java.sql.*;

/**
 * Created by hotjoyit on 2016-07-19
 */
public abstract class UserDao {
  // 관심사의 분리 : Connection 생성기능을 클래스로 분리
  private SimpleConnectionMaker simpleConnectionMaker;

  public UserDao(){
    this.simpleConnectionMaker = new SimpleConnectionMaker();
  }

  public void add(User user) throws ClassNotFoundException, SQLException {
    Connection c= simpleConnectionMaker.makeNewConnection();

    PreparedStatement ps = c.prepareStatement(
        "insert into users(id, name, password) values(?, ?, ?)");
    ps.setString(1, user.getId());
    ps.setString(2, user.getName());
    ps.setString(3, user.getPassword());

    ps. executeUpdate();

    ps.close();
    c.close();
  }

  public User get(String id)throws ClassNotFoundException, SQLException {
    Connection c = simpleConnectionMaker.makeNewConnection();

    PreparedStatement ps = c.prepareStatement(
        "select * from users where id = ?");
    ps.setString(1, id);

    ResultSet rs = ps.executeQuery();
    rs.next();
    User user = new User();
    user.setId(rs.getString("id"));
    user.setName(rs.getString("name"));
    user.setPassword(rs.getString("password"));

    rs.close();
    ps.close();
    c.close();

    return user;
  }

}
