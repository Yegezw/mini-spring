package com.minis.aop.advisor;

import com.minis.aop.config.Pointcut;

/**
 * 切入点增强器
 */
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();
}
