package me.hotjoyit.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by hotjoyit on 2016-07-20
 */
public class DeleteAllStatement implements StatementStrategy {
  @Override
  public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
    PreparedStatement ps = c.prepareStatement("delete from users");
    return ps;
  }
}
