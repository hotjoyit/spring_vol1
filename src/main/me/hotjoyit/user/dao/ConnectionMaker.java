package me.hotjoyit.user.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by hotjoyit on 2016-07-19
 */
public interface ConnectionMaker {
  Connection makeConnection() throws ClassNotFoundException, SQLException;
}
