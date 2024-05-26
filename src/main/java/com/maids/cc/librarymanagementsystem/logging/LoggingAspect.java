package com.maids.cc.librarymanagementsystem.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.maids.cc.librarymanagementsystem.*.*(..))")
    public void logBeforeMethod() {
        logger.info("A method is about to be called");
    }

    @After("execution(* com.maids.cc.librarymanagementsystem.*.*(..))")
    public void logAfterMethod() {
        logger.info("A method has just been called");
    }

    @AfterThrowing(pointcut = "execution(* com.maids.cc.librarymanagementsystem.*.*(..))", throwing = "exception")
    public void logExceptions(Exception exception) {
        logger.error("An exception has been thrown: ", exception);
    }

    @Around("execution(* com.maids.cc.librarymanagementsystem.*.*(..))")
    public Object logPerformanceMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        logger.info(joinPoint.getSignature() + " executed in " + (endTime - startTime) + "ms");
        return result;
    }
}

