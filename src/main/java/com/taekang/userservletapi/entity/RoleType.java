package com.taekang.userservletapi.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleType {
  VIP,
  BASIC;

  @JsonCreator
  public static EmployeeType fromString(String role) {
    for (EmployeeType type : EmployeeType.values()) {
      if (type.name().equalsIgnoreCase(role)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Invalid RoleType: " + role);
  }

  @JsonValue
  public String toValue() {
    return name().toLowerCase(); // JSON으로 출력될 때 소문자로 변환
  }
}
