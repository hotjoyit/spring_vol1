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
    List<User> users = userDao.getAll();
    for (User user : users) {
      Boolean changed = null;
      if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
        user.setLevel(Level.SILVER);
        changed = true;
      } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
        user.setLevel(Level.GOLD);
        changed = true;
      } else if (user.getLevel() == Level.GOLD) {
        changed = false;
      } else {
        changed = false;
      }

      if (changed) {
        userDao.update(user);
      }
    }
  }
}
