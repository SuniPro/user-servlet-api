package com.taekang.userservletapi.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_statements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private BigDecimal deposit;
    private BigDecimal withdrawal;
    private LocalDateTime transactionTime;

    public BankStatement(User user, BigDecimal deposit, BigDecimal withdrawal) {
        this.user = user;
        this.deposit = deposit;
        this.withdrawal = withdrawal;
        this.transactionTime = LocalDateTime.now();
    }
}
