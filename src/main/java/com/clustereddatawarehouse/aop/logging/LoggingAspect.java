package com.clustereddatawarehouse.aop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut(
        "within(@org.springframework.stereotype.Repository *)" +
        " || within(@org.springframework.stereotype.Service *)" +
        " || within(@org.springframework.web.bind.annotation.RestController *)"
    )
    public void springBeanPointcut() {
        //spring component pointcut
    }

    @Pointcut(
        "within(com.clustereddatawarehouse.repository..*)" +
        " || within(com.clustereddatawarehouse.service..*)" +
        " || within(com.clustereddatawarehouse.controller..*)"
    )
    public void applicationPackagePointcut() {
        //package pointcut
    }

    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void wrapException(JoinPoint joinPoint, Throwable e) {
        logger(joinPoint).error( "Exception in {}() with cause = {}", joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL" );
    }

    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object wrapLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        log.info("Class : {}", joinPoint.getSignature().getDeclaringTypeName());
        log.debug("Enter: {}() ", joinPoint.getSignature().getName());
        try {
            Object result = joinPoint.proceed();
            log.debug("Exit: {}() with result = {}", joinPoint.getSignature().getName(), result);
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
