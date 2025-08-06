package com.taekang.userservletapi.entity;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum RoleType {
  ADMINISTRATOR(0),
  GUEST(1),
  USER(2),
  VIP(3);

  private final int code;

  RoleType(int code) {
    this.code = code;
  }

  /** ADMINISTRATOR == 0 GUEST == 1 USER == 2 VIP == 3 */
  public static RoleType fromCode(int code) {
    return Arrays.stream(values())
        .filter(l -> l.code == code)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unknown UserLevel: " + code));
  }
}
