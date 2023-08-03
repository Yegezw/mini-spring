package com.minis.aop.config;

/**
 * 切入点
 */
public interface Pointcut {

	MethodMatcher getMethodMatcher();
}
