package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.User;

import java.util.List;

/**
 * Created by hotjoyit on 2016-07-19
 */
public interface UserDao {

  void add(final User user);

  User get(String id);

  void deleteAll();

  int getCount();

  List<User> getAll();
}
