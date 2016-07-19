package me.hotjoyit.user.dao;

import me.hotjoyit.user.domain.User;

import java.sql.SQLException;

/**
 * Created by hotjoyit on 2016-07-19
 */
public class UserDaoTestDrive {

  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    ConnectionMaker connectionMaker = new ZConnectionMaker();
    UserDao dao = new UserDao(connectionMaker);

    User user= new User();
    user.setId("hotjoyit");
    user.setName("조우현");
    user.setPassword("single");

    dao.add(user);

    System.out.println(user.getId() + " 등록 성공");

    User user2 = dao.get(user.getId());
    System.out.println(user2.getName());
    System.out.println(user2.getPassword());

    System.out.println(user2.getId() + " 조회 성공");
  }
}
