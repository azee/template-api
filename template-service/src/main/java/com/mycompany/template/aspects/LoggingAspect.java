package com.mycompany.template.aspects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: azee
 * Date: 10/9/13
 * Time: 2:11 PM
  */
@Component
@Aspect
public class LoggingAspect {
    private final Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Around("@within(com.mycompany.template.aspects.Loggable) || @annotation(com.mycompany.template.aspects.Loggable)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable{
        long started = new Date().getTime();
        Object serviceGreeting = joinPoint.proceed();
        long processTime = new Date().getTime() - started;
        logger.info("Processed [" + joinPoint.getSignature().getName() + "] for [" +
                processTime + "] ms.");
        return serviceGreeting;

    }
}