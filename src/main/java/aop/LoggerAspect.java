package aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggerAspect {
    @Pointcut("execution(* keyboard..controller.*Controller.*(..)) || execution(* keyboard..service.*ServiceImpl.*(..)) || excution(* keyboard..mapper.*Mapper.*(..))")
    private void loggerTarget() {
    }

    @Around("loggerTarget()")
    public Object logPrinter(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.debug(className + "." + methodName + "()");
        return joinPoint.proceed();
    }
}
