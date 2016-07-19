package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.User;

import java.sql.*;

/**
 * Created by hotjoyit on 2016-07-19
 */
public abstract class UserDao {
  // 같은 코드가 중복으로 등장하는 문제, 하나의 메소드에서 여러 기능을 수행하는 문제가 존재
  public void add(User user) throws ClassNotFoundException, SQLException {
    Connection c= getConnection();

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
    Connection c = getConnection();

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

  // 템플릿 메소드 패턴 
  public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

}
