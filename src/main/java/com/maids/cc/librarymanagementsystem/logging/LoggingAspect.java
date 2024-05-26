package com.maids.cc.librarymanagementsystem.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.maids.cc.librarymanagementsystem.book.service.BookService.*(..)))")
    public void bookServicePointCut() {}

    @Pointcut("execution(* com.maids.cc.librarymanagementsystem.patron.service.PatronService.*(..)))")
    public void patronServicePointCut() {}

    @Pointcut("execution(* com.maids.cc.librarymanagementsystem.borrowingrecord.service.BorrowingRecordService.*(..)))")
    public void borrowingRecordServicePointCut() {}

    @Around("bookServicePointCut() || patronServicePointCut() || borrowingRecordServicePointCut()")
    public Object logPerformanceMetrics(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().toString();
        Object [] methodArguments = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        logger.info("Method invoked : ClassName = " + className + ": MethodName = " + methodName + ": methodArguments = "
                + Arrays.toString(methodArguments));
        Object responseObject = joinPoint.proceed();
        logger.info("Method invoked : ClassName = " + className + ": MethodName = " + methodName + ": methodResponse = "
                + responseObject);
        logger.info(joinPoint.getSignature().getName() + " executed in " + (endTime - startTime) + "ms");
        return responseObject;
    }
}

