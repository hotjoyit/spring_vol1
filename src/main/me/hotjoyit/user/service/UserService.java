package me.hotjoyit.user.service;

import me.hotjoyit.user.dao.UserDao;
import me.hotjoyit.user.domain.Level;
import me.hotjoyit.user.domain.User;

import java.util.List;

/**
 * Created by hotjoyit on 2016-07-21
 */
public class UserService {
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

  private void upgradeLevel(User user) {
    user.upgradeLevel();
    userDao.update(user);
  }

  private boolean canUpgradeLevel(User user) {
    Level currentLevel = user.getLevel();
    switch (currentLevel) {
      case BASIC:
        return (user.getLogin() >= 50);
      case SILVER:
        return (user.getRecommend() >= 30);
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
