package com.taekang.userservletapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestRequestService {

  private final RestTemplate restTemplate;

  @Autowired
  public RestRequestService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Object getRequestEntity(String url) {
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

    return responseEntity.getBody();
  }
}
