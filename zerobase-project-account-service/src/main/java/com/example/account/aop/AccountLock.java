package com.example.account.aop;

import java.lang.annotation.*;

/**
 * 어노테이션을 커스텀으로 만든다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AccountLock {
    long tryLockTime() default 5000L;
}
