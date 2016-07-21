package me.hotjoyit.user.domain;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by hotjoyit on 2016-07-21
 */
public class UserTest {

  private User user;

  @Before
  public void setUp() {
    this.user = new User();
  }

  @Test
  public void upgradeLevel() {
    Level[] levels = Level.values();

    for (Level level : levels) {
      if (level.nextLevel() == null) {
        continue;
      }
      user.setLevel(level);
      user.upgradeLevel();
      assertThat(user.getLevel(), is(level.nextLevel()));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotUpgradeLevel() {
    Level[] levels = Level.values();

    for (Level level :levels) {
      if (level.nextLevel() != null) {
        continue;
      }
      user.setLevel(level);
      user.upgradeLevel();
    }
  }
}