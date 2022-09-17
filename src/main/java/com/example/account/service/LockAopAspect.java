package com.example.account.service;

import com.example.account.aop.AccountLockIdInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


/**
 * redis에서 lock 부분 구현
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;

    @Around("@annotation(com.example.account.aop.AccountLock) && args(request)") // 해당 어노테이션이 붙었을 때 메서드를 발동함
    public Object aroundMethod(
            ProceedingJoinPoint pjp,
            AccountLockIdInterface request
    ) throws Throwable {
        // lock 취득 시도
        lockService.lock(request.getAccountNumber());
        try{
            return pjp.proceed(); // aop 걸었던 부분을 동작시킴
        } finally {
            // 동작의 실패,성공 여부와 상관없이 lock 해제
            lockService.unlock(request.getAccountNumber());
        }
    }

}
