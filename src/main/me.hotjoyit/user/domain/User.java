package me.hotjoyit.user.domain;

/**
 * Created by hotjoyit on 2016-07-19
 */
public class User {
  private String id;
  private String name;
  private String password;

  public User() {

  }

  public User(String id, String name, String password) {
    this.id = id;
    this.name = name;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
