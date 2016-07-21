package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by hotjoyit on 2016-07-20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "/applicationContext.xml")
public class UserDaoTest {

  // UserDaoTest 객체는 Test마다 새로 생성하지만 JUnit 테스트 컨텍스트 프레임워크에서 관리하는 Bean들은 한 번만 생성된다
  @Autowired
  private ApplicationContext context;
  @Autowired
  private UserDao dao;

  User user1 = new User("no1", "홍길동", "pw01");
  User user2 = new User("no2", "임꺽정", "pw02");
  User user3 = new User("no3", "손오공", "pw03");

  @Test
  public void addAndGet() throws SQLException {
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    User userget1 = dao.get(user1.getId());
    checkSameUser(user1, userget1);

    User userget2 = dao.get(user2.getId());
    checkSameUser(user2, userget2);
  }

  @Test
  public void count() throws SQLException {

    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    assertThat(dao.getCount(), is(1));

    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    dao.add(user3);
    assertThat(dao.getCount(), is(3));
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void getUserFailure() throws SQLException {
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.get("unknown_id");
  }

  @Test
  public void getAllIdAscending() throws SQLException {
    dao.deleteAll();

    dao.add(user1);
    List<User> users1 = dao.getAll();
    assertThat(users1.size(), is(1));
    checkSameUser(user1, users1.get(0));

    dao.add(user3);
    List<User> users2 = dao.getAll();
    assertThat(users2.size(), is(2));
    checkSameUser(user1, users1.get(0));

    dao.add(user2);
    List<User> users3 = dao.getAll();
    assertThat(users3.size(), is(3));
    checkSameUser(user2, users3.get(1));
  }

  @Test
  public void getAllEmpty() throws SQLException {
    dao.deleteAll();
    List<User> users = dao.getAll();

    assertThat(users.size(), is(0));
  }

  private void checkSameUser(User user1, User user2) {
    assertThat(user1.getId(), is(user2.getId()));
    assertThat(user1.getName(), is(user2.getName()));
    assertThat(user2.getPassword(), is(user2.getPassword()));
  }
}