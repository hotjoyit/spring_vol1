package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by hotjoyit on 2016-07-19
 */
public class UserDao {
  private DataSource dataSource;

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void add(User user) throws SQLException {
    StatementStrategy st = new AddStatement(user);
    jdbcContextWithStatementStrategy(st);
  }

  public User get(String id)throws SQLException {
    Connection c = dataSource.getConnection();

    PreparedStatement ps = c.prepareStatement(
        "select * from users where id = ?");
    ps.setString(1, id);
    ResultSet rs = ps.executeQuery();

    User user = null;
    if (rs.next()) {
      user = new User();
      user.setId(rs.getString("id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
    }
    rs.close();
    ps.close();
    c.close();

    if (user == null) {
      throw new SQLDataException();
    }

    return user;
  }

  public void deleteAll() throws SQLException {
    StatementStrategy st = new DeleteAllStatement();
    jdbcContextWithStatementStrategy(st);
  }

  public int getCount() throws SQLException {
    Connection c= dataSource.getConnection();
    PreparedStatement ps = c.prepareStatement("select count(id) from users");

    ResultSet rs = ps.executeQuery();
    rs.next();
    int count = rs.getInt(1);

    rs.close();
    ps.close();
    c.close();

    return count;
  }

  public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
    Connection c = null;
    PreparedStatement ps = null;

    try {
      c = dataSource.getConnection();
      ps = stmt.makePreparedStatement(c);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw e;
    } finally {
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {

        }
      }
      if (c != null) {
        try {
          c.close();
        } catch (SQLException e) {

        }
      }
    }
  }
}
