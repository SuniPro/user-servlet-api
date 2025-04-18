package com.taekang.userservletapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 빌더 사용 시 필수
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  private BigDecimal money;
  private BigDecimal sportsMoney;
  private BigDecimal casinoMoney;

  public void deposit(BigDecimal amount) {
    this.money = this.money.add(amount);
  }

  public void withdraw(BigDecimal amount) {
    if (this.money.compareTo(amount) < 0) {
      throw new IllegalArgumentException("잔액 부족");
    }
    this.money = this.money.subtract(amount);
  }

  public void betOnSports(BigDecimal amount) {
    withdraw(amount);
    this.sportsMoney = this.sportsMoney.add(amount);
  }

  public void betOnCasino(BigDecimal amount) {
    withdraw(amount);
    this.casinoMoney = this.casinoMoney.add(amount);
  }
}
