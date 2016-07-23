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
import java.util.Arrays;
import java.util.List;

import static me.hotjoyit.user.service.UserService.MIN_LOGIN_COUNT_FOR_SILVER;
import static me.hotjoyit.user.service.UserService.MIN_RECOMMEND_COUNT_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by hotjoyit on 2016-07-21
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class UserServiceTest {

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
    userDao.deleteAll();

    for (User user : users) {
      userDao.add(user);
    }

    userService.upgradeLevels();
    checkLevelUpgraded(users.get(0), false);
    checkLevelUpgraded(users.get(1), true);
    checkLevelUpgraded(users.get(2), false);
    checkLevelUpgraded(users.get(3), true);
    checkLevelUpgraded(users.get(4), false);
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
    UserService testUserService = new UserService.TestUserService(users.get(3).getId());
    testUserService.setUserDao(this.userDao);
    testUserService.setTransactionManager(transactionManager);

    userDao.deleteAll();
    for (User user : users) {
      userDao.add(user);
    }
    try {
      testUserService.upgradeLevels();
      fail("TestUserServiceException expected");
    } catch (UserService.TestUserServiceException e) {

    }

    checkLevelUpgraded(users.get(1), false);
  }
}