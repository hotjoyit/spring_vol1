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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

  List<User> users;

  @Before
  public void setUp() {
    users = Arrays.asList(
        new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
        new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
        new User("erwins", "신승환", "p3", Level.SILVER, 60, 29),
        new User("madinite1", "상호리", "p4", Level.SILVER, 60, 30),
        new User("greeen", "민귤", "p5", Level.GOLD, 100, 100)
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
  public void upgradeLevels() {
    userDao.deleteAll();

    for (User user : users) {
      userDao.add(user);
    }

    userService.upgradeLevels();
    checkLevel(users.get(0), Level.BASIC);
    checkLevel(users.get(1), Level.SILVER);
    checkLevel(users.get(2), Level.SILVER);
    checkLevel(users.get(3), Level.GOLD);
    checkLevel(users.get(4), Level.GOLD);
  }

  private void checkLevel(User user, Level expectedLevel) {
    User userUpdate = userDao.get(user.getId());
    assertThat(userUpdate.getLevel(), is(expectedLevel));
  }
}