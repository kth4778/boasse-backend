package hello.boassebackend.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // 도메인 하위의 모든 Controller 메서드 타겟
    @Around("execution(* hello.boassebackend.domain..controller..*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        // 요청 로그 (파라미터 등은 너무 길어질 수 있으므로 생략하거나 필요한 경우 추가)
        // log.info("{} - {} 요청 시작", className, methodName); 
        
        try {
            Object result = joinPoint.proceed();
            
            stopWatch.stop();
            long time = stopWatch.getTotalTimeMillis();

            // 응답 로그 (성공)
            log.info("{} - {} Executed in {}ms", className, methodName, time);
            
            return result;
        } catch (Exception e) {
            stopWatch.stop();
            // 에러 발생 시 로그
            log.error("{} - {} Failed: {} ({}ms)", className, methodName, e.getClass().getSimpleName(), stopWatch.getTotalTimeMillis());
            throw e;
        }
    }

    // 도메인 하위의 모든 Service 메서드 타겟 (느린 쿼리 감지용)
    @Around("execution(* hello.boassebackend.domain..service..*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            long time = stopWatch.getTotalTimeMillis();
            
            // 1초 이상 걸리면 WARN 로그 (Slow Query Alert)
            if (time > 1000) {
                String className = joinPoint.getTarget().getClass().getSimpleName();
                String methodName = joinPoint.getSignature().getName();
                log.warn("느린 작업 감지: {}.{} executionTime={}ms, threshold=1000ms", 
                        className, methodName, time);
            }
        }
    }
}
