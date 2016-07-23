package me.hotjoyit.user.service;

import me.hotjoyit.user.domain.User;

/**
 * Created by hotjoyit on 2016-07-23
 */
public interface UserService {
  void add(User user);
  void upgradeLevels();
}
