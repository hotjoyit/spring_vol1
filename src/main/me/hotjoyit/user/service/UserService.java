package me.hotjoyit.user.service;

import me.hotjoyit.user.domain.User;

import java.util.List;

/**
 * Created by hotjoyit on 2016-07-23
 */
public interface UserService {
  void add(User user);
  void upgradeLevels();
  User get(String id);
  void deleteAll();
  int getCount();
  List<User> getAll();
  void update(User user);
}
