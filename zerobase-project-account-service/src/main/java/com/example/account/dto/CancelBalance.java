package com.example.account.dto;

import com.example.account.aop.AccountLockIdInterface;
import com.example.account.type.TransactionResultType;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;


public class CancelBalance {


    /**
     * {
     * "transactionId":"c2033bb6d82a4250aecf8e27c49b63f6",
     * "accountNumber":"1000000000",
     * "amount":1000
     * }
     */
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request implements AccountLockIdInterface {
        // LockAopAspect에서 컨트롤러의 request를 useBalance.request, CreateAccount.request 등 상관 없이 하나의 타입으로 받아오기 위해서
        // 인터페이스를 사용했음. 이럴 경우 전부 AccountLockIdInterface request로 변수를 선언하여 받아오면 되기 때문.
        // request들에는 모두 getAccountNumber가 있기 때문에 인터페이스에 있는 getAccountNumber로도 적용 가능함.
        // 따라서 각 request 메서드들에 들어오는 accountNumber의 값이 인터페이스의 getAccountNumber가 되는 것임
        @NotBlank
        private String transactionId;

        @NotBlank
        @Size(min = 10, max = 10)
        private String accountNumber;

        @NotNull
        @Min(10)
        @Max(1000_000_000)
        private Long amount;
    }


    /**
     * {
     * "accountNumber":"1000000000",
     * "transactionResult":"S",
     * "transactionId":"5d011bb6d82cc50aecf8e27cdabb6772",
     * "amount":1000,
     * "transactedAt":"2022-06-01T23:26:14.671859"
     * }
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String accountNumber;
        private TransactionResultType transactionResult;
        private String transactionId;
        private Long amount;
        private LocalDateTime registeredAt;

        public static Response from(TransactionDto transactionDto) {
            return Response.builder()
                    .accountNumber(transactionDto.getAccountNumber())
                    .transactionResult(transactionDto.getTransactionResultType())
                    .transactionId(transactionDto.getTransactionId())
                    .amount(transactionDto.getAmount())
                    .registeredAt(transactionDto.getTransactedAt())
                    .build();
        }
    }
}

