package me.hotjoyit.user.service;

import me.hotjoyit.user.dao.UserDao;
import me.hotjoyit.user.domain.Level;
import me.hotjoyit.user.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static me.hotjoyit.user.service.UserServiceImpl.MIN_LOGIN_COUNT_FOR_SILVER;
import static me.hotjoyit.user.service.UserServiceImpl.MIN_RECOMMEND_COUNT_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

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
  private UserService testUserService;
  @Autowired
  private PlatformTransactionManager transactionManager;
  @Autowired
  private ApplicationContext context;

  private List<User> users;

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
    UserDao mockUserDao = mock(UserDao.class);
    when(mockUserDao.getAll()).thenReturn(users);
    userService.setUserDao(mockUserDao);

    userService.upgradeLevels();

    verify(mockUserDao, times(2)).update(any(User.class));
    verify(mockUserDao).update(users.get(1));
    assertThat(users.get(1).getLevel(), is(Level.SILVER));
    verify(mockUserDao).update(users.get(3));
    assertThat(users.get(3).getLevel(), is(Level.GOLD));
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
  @DirtiesContext
  public void upgradeAllOrNothing() throws Exception {
    userDao.deleteAll();
    for (User user : users) {
      userDao.add(user);
    }
    try {
      testUserService.upgradeLevels();
      fail("TestUserServiceException expected");
    } catch (TestUserServiceException e) {

    }

    checkLevelUpgraded(users.get(1), false);
  }

  @Test
  public void advisorAutoProxyCreator() {
    assertThat(testUserService, is(java.lang.reflect.Proxy.class));
  }

  static class TestUserServiceImpl extends UserServiceImpl {
    private String id = "madinite1";

    protected void upgradeLevel(User user) {
      if (user.getId().equals(this.id)) {
        throw new TestUserServiceException();
      }
      super.upgradeLevel(user);
    }
  }

  static class TestUserServiceException extends RuntimeException {

  }
}