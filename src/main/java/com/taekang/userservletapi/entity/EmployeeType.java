package com.taekang.userservletapi.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EmployeeType {
  ADMIN,
  USER,
  GUEST;

  @JsonCreator
  public static EmployeeType fromString(String role) {
    return RoleType.fromString(role);
  }

  @JsonValue
  public String toValue() {
    return name().toLowerCase(); // JSON으로 출력될 때 소문자로 변환
  }
}
