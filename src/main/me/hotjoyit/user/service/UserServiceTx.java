package me.hotjoyit.user.service;

import me.hotjoyit.user.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Created by hotjoyit on 2016-07-23
 */
public class UserServiceTx implements UserService {

  private UserService userService;
  private PlatformTransactionManager transactionManager;

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Override
  public void add(User user) {
    userService.add(user);
  }

  @Override
  public void upgradeLevels() {
    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
    try {
      userService.upgradeLevels();
      transactionManager.commit(status);
    } catch (RuntimeException e) {
      transactionManager.rollback(status);
      throw e;
    }
  }
}
