package com.taekang.userservletapi.api;

import com.taekang.userservletapi.DTO.crypto.CryptoTransferResponseDTO;
import com.taekang.userservletapi.error.CannotFoundTransferException;
import com.taekang.userservletapi.error.TransferValidationFailException;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class CryptoTransferValidation {

  @Value("${api.node.crypto.tracker.uri}")
  private String uri;

  private final String VALIDATION_PATH = "/validation/get/transfer/";

  private final RestTemplate restTemplate;

  @Autowired
  public CryptoTransferValidation(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public CryptoTransferResponseDTO TronTransferValidation(
      String fromAddress, String toAddress, Long requestedAt) {
    URI requestUri =
        UriComponentsBuilder.fromUriString(uri)
            .path(VALIDATION_PATH + "tron")
            .queryParam("fromAddress", fromAddress)
            .queryParam("toAddress", toAddress)
            .queryParam("requestedAt", requestedAt)
            .encode()
            .build()
            .toUri();

    log.info(
        "[TronWalletValidation] Request URI: {}, FromAddress={}, ToAddress={}, RequestedAt={}",
        requestUri,
        fromAddress,
        toAddress,
        requestedAt);

    try {
      ResponseEntity<CryptoTransferResponseDTO> responseEntity =
          restTemplate.getForEntity(
              requestUri, CryptoTransferResponseDTO.class); // [수정] requestUri 사용

      // 1. 응답 본문(body)이 null인지 확인합니다.
      if (responseEntity.getBody() == null) {
        log.warn("[TronWalletValidation] Response body is null for URI: {}", requestUri);
        throw new CannotFoundTransferException();
      }

      log.info("[TronWalletValidation] Response: {}", responseEntity.getBody());
      return responseEntity.getBody();

    } catch (RestClientException e) {
      // 2. RestTemplate에서 발생하는 모든 예외(4xx, 5xx, 타임아웃 등)를 처리합니다.
      log.error("[TronWalletValidation] API call failed for URI: {}", requestUri, e);
      throw new CannotFoundTransferException();
    } catch (CannotFoundTransferException e) {
      throw new CannotFoundTransferException();
    } catch (Exception e) {
      throw new TransferValidationFailException();
    }
  }

  public CryptoTransferResponseDTO EthTransferValidation(
      String fromAddress, String toAddress, Long requestedAt) {
    URI requestUri =
        UriComponentsBuilder.fromUriString(uri)
            .path(VALIDATION_PATH + "eth")
            .queryParam("fromAddress", fromAddress)
            .queryParam("toAddress", toAddress)
            .queryParam("requestedAt", requestedAt)
            .encode()
            .build()
            .toUri();

    log.info(
        "[EthTransferValidation] Request URI: {}, FromAddress={}, ToAddress={}, RequestedAt={}",
        requestUri,
        fromAddress,
        toAddress,
        requestedAt);

    ResponseEntity<CryptoTransferResponseDTO> responseEntity =
        restTemplate.getForEntity(uri, CryptoTransferResponseDTO.class);
    log.info("[EthTransferValidation] Response: {}", responseEntity.getBody());

    return responseEntity.getBody();
  }
}
