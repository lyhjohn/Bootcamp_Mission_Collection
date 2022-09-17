package com.example.account.domain;

import com.example.account.exception.AccountException;
import com.example.account.service.TransactionService;
import com.example.account.type.ErrorCode;
import lombok.*;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.account.type.AccountStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private AccountUser accountUser;
    private String accountNumber;
    
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    private Long balance;
    
    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    private Long payAmount;

    @CreatedDate
	private LocalDateTime createdAt;
    @LastModifiedDate
	private LocalDateTime updatedAt;

    public void useBalance(Long amount) {
        if (amount > balance) {
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }

        balance -= amount;
        payAmount += amount;
    }

    public void cancelBalance(Long amount) {
        if (amount < 0) {
            throw new AccountException(ErrorCode.INVALID_REQUEST);
        }
        balance += amount;
        payAmount -= amount;
    }
}
