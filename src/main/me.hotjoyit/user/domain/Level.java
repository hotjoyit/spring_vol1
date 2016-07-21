package me.hotjoyit.user.domain;

/**
 * Created by hotjoyit on 2016-07-21
 */
public enum Level {
  BASIC(1), SILVER(2), GOLD(3);

  private final int value;

  Level(int value) {
    this.value = value;
  }

  public int intValue() {
    return value;
  }

  public static Level valueOf(int value) {
    switch (value) {
      case 1: return BASIC;
      case 2: return SILVER;
      case 3: return GOLD;
      default: throw new AssertionError("Unknow Value for Level : " +value);
    }
  }
}
