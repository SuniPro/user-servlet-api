package com.taekang.userservletapi.error;

public class AbilityNotFoundException extends BusinessException {
  public AbilityNotFoundException() {
    super(ErrorCode.ABILITY_IS_EMPTY);
  }
}
