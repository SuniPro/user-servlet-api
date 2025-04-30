package com.taekang.userservletapi.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NeverBounceEmailVerifyResponseDTO(
    String status,
    String result,
    List<String> flags,
    String suggested_correction,
    boolean success) {}
