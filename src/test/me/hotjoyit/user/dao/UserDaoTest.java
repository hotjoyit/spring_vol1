package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by hotjoyit on 2016-07-20
 */
public class UserDaoTest {

  @Test
  public void addAndGet() throws SQLException {
    ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);

    UserDao dao = context.getBean("userDao", UserDao.class);

    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    User user = new User();
    user.setId("hotjoyit");
    user.setName("조우현");
    user.setPassword("single");

    dao.add(user);
    assertThat(dao.getCount(), is(1));

    User user2 = dao.get(user.getId());

    assertThat(user2.getName(), is(user.getName()));
    assertThat(user2.getPassword(), is(user.getPassword()));
  }
}