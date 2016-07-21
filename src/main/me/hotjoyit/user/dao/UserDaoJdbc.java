package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.Level;
import me.hotjoyit.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by hotjoyit on 2016-07-21
 */
public class UserDaoJdbc implements UserDao {
  private JdbcTemplate jdbcTemplate;

  private RowMapper<User> userMapper = new RowMapper<User>() {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setId(rs.getString("id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
      user.setLevel(Level.valueOf(rs.getInt("level")));
      user.setLogin(rs.getInt("login"));
      user.setRecommend(rs.getInt("recommend"));
      return user;
    }
  };

  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public void add(final User user) {
    jdbcTemplate.update(
        "insert into users(id, name, password, level, login, recommend) values(?,?,?,?,?,?)"
        , user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
  }

  @Override
  public User get(String id) {
    return jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, this.userMapper);
  }

  @Override
  public void deleteAll() {
    jdbcTemplate.update("delete from users");
  }

  @Override
  public int getCount() {
    return jdbcTemplate.queryForInt("select count(*) from users");
  }

  @Override
  public List<User> getAll() {
    return jdbcTemplate.query("select * from users order by id", userMapper);
  }

  @Override
  public void update(User user) {
    jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?, recommend = ? " +
                        "where id = ?", user.getName(), user.getPassword(), user.getLevel().intValue()
                          , user.getLogin(), user.getRecommend(), user.getId());
  }
}