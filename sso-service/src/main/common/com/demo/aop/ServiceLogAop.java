package com.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/**
 * @author bale
 */
@Slf4j
@Aspect
@Service
public class ServiceLogAop {

    @Pointcut("execution(public * com.demo.service.*.*(com.demo.servlet.RestfulContext,..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        log.info("{} invoked, input args:{}", joinPoint.getSignature().toShortString(), args);

        Object proceed = null;
        try {
            proceed = joinPoint.proceed(args);
        } catch (Throwable throwable) {
            log.error("{} invoked exception:{}", joinPoint.getSignature().toShortString(), throwable.getMessage());
        }
        return proceed;
    }

    @Around("@annotation(com.demo.annotation.CostStatistics)")
    public Object costAround(ProceedingJoinPoint joinPoint) {
        Object proceed = null;
        final Object[] args = joinPoint.getArgs();
        try {
            long start = System.currentTimeMillis();
            proceed = joinPoint.proceed(args);
            log.info("{} invoked, cost:{}", joinPoint.getSignature().toShortString(),
                    System.currentTimeMillis() - start);
        } catch (Throwable throwable) {
            log.error("{} invoked exception:{}", joinPoint.getSignature().toShortString(), throwable.getMessage());
        }
        return proceed;
    }

}
