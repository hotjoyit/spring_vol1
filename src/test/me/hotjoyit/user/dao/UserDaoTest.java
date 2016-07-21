package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.Level;
import me.hotjoyit.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
  private User user1, user2, user3;

  @Before
  public void setUp() {
    user1 = new User("no1", "홍길동", "pw01", Level.BASIC, login(1), recommend(0));
    user2 = new User("no2", "임꺽정", "pw02", Level.SILVER, login(55), recommend(10));
    user3 = new User("no3", "손오공", "pw03", Level.GOLD, login(100), recommend(40));
  }

  @Test
  public void addAndGet() {
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
  public void count() {

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
  public void getUserFailure() {
    dao.deleteAll();
    assertThat(dao.getCount(), is(0));

    dao.get("unknown_id");
  }

  @Test
  public void getAllIdAscending() {
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
  public void getAllEmpty() {
    dao.deleteAll();
    List<User> users = dao.getAll();

    assertThat(users.size(), is(0));
  }

  private void checkSameUser(User user1, User user2) {
    assertThat(user1.getId(), is(user2.getId()));
    assertThat(user1.getName(), is(user2.getName()));
    assertThat(user2.getPassword(), is(user2.getPassword()));
    assertThat(user2.getLevel(), is(user2.getLevel()));
    assertThat(user2.getLogin(), is(user2.getLogin()));
    assertThat(user2.getRecommend(), is(user2.getRecommend()));
  }

  @Test(expected = DuplicateKeyException.class)
  public void duplicatedInsert() {
    dao.deleteAll();

    dao.add(user1);
    dao.add(user1);
  }

  @Test
  public void update() {
    dao.deleteAll();
    dao.add(user1);
    dao.add(user2); // 수정하지 않을 User

    user1.setName("빙구");
    user1.setPassword("빙구빙구");
    user1.setLevel(Level.GOLD);
    user1.setLogin(21124);
    user1.setRecommend(31233);

    dao.update(user1);
    User updated = dao.get(user1.getId());
    checkSameUser(updated, user1);

    User notUpdated = dao.get(user2.getId());
    checkSameUser(notUpdated, user2);
  }

  private int login(int n) {
    return n;
  }

  private int recommend(int n) {
    return n;
  }
}