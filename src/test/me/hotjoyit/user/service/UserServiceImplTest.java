package me.hotjoyit.user.service;

import me.hotjoyit.user.dao.UserDao;
import me.hotjoyit.user.domain.Level;
import me.hotjoyit.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.hotjoyit.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static me.hotjoyit.user.service.UserServiceImpl.MIN_RECOMMEND_COUNT_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by hotjoyit on 2016-07-21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class UserServiceImplTest {

  @Autowired
  private UserDao userDao;
  @Autowired
  private UserService userService;
  @Autowired
  private PlatformTransactionManager transactionManager;

  List<User> users;

  @Before
  public void setUp() {
    users = Arrays.asList(
        new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER - 1, 0),
        new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGIN_COUNT_FOR_SILVER, 0),
        new User("erwins", "신승환", "p3", Level.SILVER, 60, MIN_RECOMMEND_COUNT_FOR_GOLD - 1),
        new User("madinite1", "상호리", "p4", Level.SILVER, 60, MIN_RECOMMEND_COUNT_FOR_GOLD),
        new User("greeen", "민귤", "p5", Level.GOLD, 100, Integer.MAX_VALUE)
    );
  }

  @Test
  public void add() {
    userDao.deleteAll();

    User userWithLevel = users.get(4);
    User userWithoutLevel = users.get(0);
    userWithoutLevel.setLevel(null);

    userService.add(userWithLevel);
    userService.add(userWithoutLevel);

    User userWithLevelRead = userDao.get(userWithLevel.getId());
    User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

    assertThat(userWithLevel.getLevel(), is(userWithLevelRead.getLevel()));
    assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
  }

  @Test
  public void upgradeLevels() throws SQLException {
    UserServiceImpl userService = new UserServiceImpl();
    MockUserDao mockUserDao = new MockUserDao(users);
    userService.setUserDao(mockUserDao);

    userService.upgradeLevels();

    List<User> updated = mockUserDao.getUpdated();
    assertThat(updated.size(), is(2));
    checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
    checkUserAndLevel(updated.get(1), "madinite1", Level.GOLD);
  }

  private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
    assertThat(updated.getId(), is(expectedId));
    assertThat(updated.getLevel(), is(expectedLevel));
  }

  private void checkLevelUpgraded(User user, boolean upgraded) {
    User userUpdated = userDao.get(user.getId());
    if (upgraded) {
      assertThat(userUpdated.getLevel(), is(user.getLevel().nextLevel()));
    } else {
      assertThat(userUpdated.getLevel(), is(user.getLevel()));
    }
  }

  @Test
  public void upgradeAllOrNothing() throws SQLException {
    UserServiceImpl.TestUserServiceImpl testUserServiceImpl = new UserServiceImpl.TestUserServiceImpl(users.get(3).getId());
    testUserServiceImpl.setUserDao(this.userDao);

    UserServiceTx userSerivceTx = new UserServiceTx();
    userSerivceTx.setTransactionManager(transactionManager);
    userSerivceTx.setUserService(testUserServiceImpl);

    userDao.deleteAll();
    for (User user : users) {
      userDao.add(user);
    }
    try {
      userSerivceTx.upgradeLevels();
      fail("TestUserServiceException expected");
    } catch (UserServiceImpl.TestUserServiceException e) {

    }

    checkLevelUpgraded(users.get(1), false);
  }

  static class MockUserDao implements UserDao {

    private List<User> users;
    private List<User> updated = new ArrayList<>();

    private MockUserDao(List<User> users) {
      this.users = users;
    }

    public List<User> getUpdated() {
      return updated;
    }

    @Override
    public List<User> getAll() {
      return users;
    }

    @Override
    public void update(User user) {
      updated.add(user);
    }

    @Override
    public void add(User user) {
      throw new UnsupportedOperationException();
    }

    @Override
    public User get(String id) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
      throw new UnsupportedOperationException();
    }
  }
}