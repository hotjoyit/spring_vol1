package me.hotjoyit.user.service;

import me.hotjoyit.user.dao.UserDao;
import me.hotjoyit.user.domain.Level;
import me.hotjoyit.user.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

/**
 * Created by hotjoyit on 2016-07-21
 */
public class UserService {
  public static final int MIN_LOGIN_COUNT_FOR_SILVER = 50;
  public static final int MIN_RECOMMEND_COUNT_FOR_GOLD = 30;

  private UserDao userDao;
  private PlatformTransactionManager transactionManager;

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  public void upgradeLevels() throws SQLException {
    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
    try {
      upgradeLevelInternal();
      transactionManager.commit(status);
    } catch (Exception e) {
      transactionManager.rollback(status);
      throw e;
    }
  }

  private void upgradeLevelInternal() {
    for (User user : userDao.getAll()) {
      if (canUpgradeLevel(user)) {
        upgradeLevel(user);
      }
    }
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

  static class TestUserService extends UserService {
    private String id;

    public TestUserService(String id) {
      this.id = id;
    }

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
