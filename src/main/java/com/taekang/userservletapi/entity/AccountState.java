package com.taekang.userservletapi.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountState {
  ACTIVE,
  INACTIVE;

  @JsonCreator
  public static AccountState fromString(String accountState) {
    for (AccountState type : AccountState.values()) {
      if (type.name().equalsIgnoreCase(accountState)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid RoleType: " + accountState);
  }

  @JsonValue
  public String toValue() {
    return name().toLowerCase(); // JSON으로 출력될 때 소문자로 변환
  }
}
