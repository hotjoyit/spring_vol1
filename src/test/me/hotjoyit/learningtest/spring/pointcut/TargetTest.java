package me.hotjoyit.learningtest.spring.pointcut;

import org.junit.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by hotjoyit on 2016-07-24
 */
public class TargetTest {

  @Test
  public void methodSignaturePointcut() throws NoSuchMethodException {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("execution(public int " +
        "me.hotjoyit.learningtest.spring.pointcut.Target.minus(int, int) " +
        "throws java.lang.RuntimeException)");

    // Target.minus()
    assertThat(pointcut.getClassFilter().matches(Target.class) &&
              pointcut.getMethodMatcher().matches(
                  Target.class.getMethod("minus", int.class, int.class), null), is(true));

    // Target.plus()
    assertThat(pointcut.getClassFilter().matches(Target.class) &&
        pointcut.getMethodMatcher().matches(
            Target.class.getMethod("plus", int.class, int.class), null), is(false));

    // Bean.method()
    assertThat(pointcut.getClassFilter().matches(Bean.class) &&
    pointcut.getMethodMatcher().matches(Target.class.getMethod("method"), null), is(false));
  }
}