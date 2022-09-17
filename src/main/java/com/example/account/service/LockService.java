package com.example.account.service;

import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.example.account.type.ErrorCode.ACCOUNT_TRANSACTION_LOCK;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {
    private final RedissonClient redissonClient;

    public void lock(String accountNumber) {
        RLock lock = redissonClient.getLock(getLockKey(accountNumber)); // Lock에 쓰는 key
        log.debug("Trying lock for accountNumber : {}", accountNumber);

        try {
            boolean isLock = lock.tryLock(1, 15, TimeUnit.SECONDS);
            // waitTime: 1초 동안 lock이 안풀리면 다시 lock을 취득 못함 / leaseTime: 5초 동안 아무 작업을 안하면 lock이 풀림
            if(!isLock) {
                log.error("======Lock acquisition failed=====");
                throw new AccountException(ACCOUNT_TRANSACTION_LOCK);
            }
        } catch (AccountException e) {
            throw e;
        } catch (Exception e) { // 우리가 모르는 예외 발생하는 경우
            log.error("Redis lock failed");
        }
    }

    public void unlock(String accountNumber) {
        log.debug("Unlock for accountNumber : {}", accountNumber);
        redissonClient.getLock(getLockKey(accountNumber)).unlock();
    }

    private String getLockKey(String accountNumber) {
        return "ACLK: " + accountNumber;
    }
}
