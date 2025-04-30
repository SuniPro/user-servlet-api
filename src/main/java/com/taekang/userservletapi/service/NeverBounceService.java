package com.taekang.userservletapi.service;

import com.taekang.userservletapi.DTO.NeverBounceEmailVerifyResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class NeverBounceService {

  @Value("${neverbounce.api.key}")
  private String apiKey;

  private final RestTemplate restTemplate;

  public NeverBounceService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public boolean isEmailValid(String email) {
    String url =
        String.format("https://api.neverbounce.com/v4/single/check?key=%s&email=%s", apiKey, email);

    try {
      var response = restTemplate.getForObject(url, NeverBounceEmailVerifyResponseDTO.class);
      return response != null && "valid".equalsIgnoreCase(response.result());
    } catch (RestClientException e) {
      throw new RuntimeException("NeverBounce API 호출 실패", e);
    }
  }
}
