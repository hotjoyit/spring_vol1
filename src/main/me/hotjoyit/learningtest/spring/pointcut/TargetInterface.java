package me.hotjoyit.learningtest.spring.pointcut;

/**
 * Created by hotjoyit on 2016-07-24
 */
public interface TargetInterface {
  void hello();
  void hello(String a);
  int minus(int a, int b) throws RuntimeException;
  int plus(int a, int b);
}
