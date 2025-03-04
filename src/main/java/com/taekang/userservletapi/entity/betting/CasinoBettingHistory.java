package com.taekang.userservletapi.entity.betting;

import com.taekang.userservletapi.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "casino_betting_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CasinoBettingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal amount; // 베팅 금액

    @Column(nullable = false)
    private LocalDateTime bettingTime; // 베팅 시간

    public CasinoBettingHistory(User user, BigDecimal amount) {
        this.user = user;
        this.amount = amount;
        this.bettingTime = LocalDateTime.now();
    }
}

