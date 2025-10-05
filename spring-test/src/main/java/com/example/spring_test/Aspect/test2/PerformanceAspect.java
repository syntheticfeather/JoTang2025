package com.example.spring_test.Aspect.test2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceAspect {

    @Around("@annotation(performanceAnnotation)")
    public Object logPerformance(ProceedingJoinPoint joinPoint, PerformanceMonitor performanceAnnotation) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        System.out.println(performanceAnnotation.value() + " took " + (end - start) + " ms");
        return result;
    }
}
