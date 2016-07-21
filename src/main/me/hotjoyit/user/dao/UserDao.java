package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Created by hotjoyit on 2016-07-19
 */
public class UserDao {
  private JdbcTemplate jdbcTemplate;

  private RowMapper<User> userMapper = new RowMapper<User>() {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setId(rs.getString("id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
      return user;
    }
  };

  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public void add(final User user) throws SQLException {
    jdbcTemplate.update(
        "insert into users(id, name, password) values(?,?,?)"
        , user.getId(), user.getName(), user.getPassword());
  }

  public User get(String id)throws SQLException {
    return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, this.userMapper);
  }

  public void deleteAll() throws SQLException {
    jdbcTemplate.update("delete from users");
  }


  public int getCount() throws SQLException {
    return jdbcTemplate.queryForInt("select count(*) from users");
  }

  public List<User> getAll() {
    return jdbcTemplate.query("select * from users order by id", userMapper);
  }
}
