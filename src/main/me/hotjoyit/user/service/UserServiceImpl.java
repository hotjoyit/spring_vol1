package me.hotjoyit.user.service;

import me.hotjoyit.user.dao.UserDao;
import me.hotjoyit.user.domain.Level;
import me.hotjoyit.user.domain.User;

import java.util.List;

/**
 * Created by hotjoyit on 2016-07-21
 */
public class UserServiceImpl implements UserService {
  public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
  public static final int MIN_RECOMMEND_COUNT_FOR_GOLD = 30;

  private UserDao userDao;

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public void upgradeLevels() {
    for (User user : userDao.getAll()) {
      if (canUpgradeLevel(user)) {
        upgradeLevel(user);
      }
    }
  }

  @Override
  public User get(String id) {
    return userDao.get(id);
  }

  @Override
  public void deleteAll() {
    userDao.deleteAll();
  }

  @Override
  public int getCount() {
    return userDao.getCount();
  }

  @Override
  public List<User> getAll() {
    return userDao.getAll();
  }

  @Override
  public void update(User user) {
    userDao.update(user);
  }

  protected void upgradeLevel(User user) {
    user.upgradeLevel();
    userDao.update(user);
  }

  private boolean canUpgradeLevel(User user) {
    Level currentLevel = user.getLevel();
    switch (currentLevel) {
      case BASIC:
        return (user.getLogin() >= MIN_LOGIN_COUNT_FOR_SILVER);
      case SILVER:
        return (user.getRecommend() >= MIN_RECOMMEND_COUNT_FOR_GOLD);
      case GOLD:
        return false;
      default:
        throw new IllegalArgumentException("Unknow Level: " + currentLevel);
    }
  }

  public void add(User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }

}
