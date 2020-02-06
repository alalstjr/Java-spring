package com.example.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerfAspect {

    /*
     * 해야할 일(advice) 생성합니다.
     * ProceedingJoinPoint advice가 적용하는 대상
     *
     * @Around() Pointcut 이름을 줄수도 있고 직접 정의할 수 도 있습니다.
     * 해당 메소드를 감싸는 형태로 적용됩니다.
     * proceed 메소드 호출 전체를 감싸고 있으므로 호출 이전과 이후의 동작을 추가하게 할 수 있습니다.
     *
     * @Around("@annotation(PerLogging)") PerLogging 에노테이션이 붙은 메소드에만 실행하는 코드선언
     * */
    // @Around("execution(* com.example..*.EventService.*(..))")
    @Around("@annotation(PerLogging)")
    public Object logPerf(ProceedingJoinPoint pjp) throws Throwable {
        long l = System.currentTimeMillis();
        Object proceed = pjp.proceed();
        System.out.println(System.currentTimeMillis() - l);
        return proceed;
    }
}
