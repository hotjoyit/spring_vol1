package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLDataException;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by hotjoyit on 2016-07-20
 */
public class UserDaoTest {

  private UserDao dao;

  @Before
  public void setUp() {
    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
    this.dao = context.getBean("userDao", UserDao.class);
  }

  @Test
  public void addAndGet() throws SQLException {
    User user1 = new User("no1", "홍길동", "pw01");
    User user2 = new User("no2", "임꺽정", "pw02");

    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    User userget1 = dao.get(user1.getId());
    assertThat(userget1.getName(), is(user1.getName()));
    assertThat(userget1.getPassword(), is(user1.getPassword()));

    User userget2 = dao.get(user2.getId());
    assertThat(userget2.getName(), is(user2.getName()));
    assertThat(userget2.getPassword(), is(user2.getPassword()));
  }

  @Test
  public void count() throws SQLException {
    User user1 = new User("no1", "홍길동", "pw01");
    User user2 = new User("no2", "임꺽정", "pw02");
    User user3 = new User("no3", "손오공", "pw03");

    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.add(user1);
    assertThat(dao.getCount(), is(1));

    dao.add(user2);
    assertThat(dao.getCount(), is(2));

    dao.add(user3);
    assertThat(dao.getCount(), is(3));
  }

  @Test(expected = SQLDataException.class)
  public void getUserFailure()throws SQLException {
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.get("unknown_id");
  }
}