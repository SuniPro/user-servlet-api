package com.taekang.userservletapi.service.financial.impl;

import com.taekang.userservletapi.error.CannotFoundedExchangeInfoException;
import com.taekang.userservletapi.service.RestRequestService;
import com.taekang.userservletapi.service.financial.ExchangeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExchangeServiceImpl implements ExchangeService {

  private final RestRequestService restRequestService;

  @Autowired
  public ExchangeServiceImpl(RestRequestService restRequestService) {
    this.restRequestService = restRequestService;
  }

  @Value("${exchange.rate.url}")
  private String exchangeUrl;

  @Value("${exchange.rate.class}")
  private String exchangeElementClass;

  @Override
  public BigDecimal getExchangeInfo() {
    try {
      Object request = restRequestService.getRequest(exchangeUrl);
      String html = request.toString();

      Document document = Jsoup.parse(html);

      Element priceElement = document.selectFirst("strong." + exchangeElementClass);
      if (priceElement == null) {
        throw new CannotFoundedExchangeInfoException();
      }

      String rawText = priceElement.text();

      String amountText = rawText.replaceAll("[^\\d.,]", "").replace(",", "");
      return new BigDecimal(amountText);
    } catch (Exception e) {
      throw new CannotFoundedExchangeInfoException();
    }
  }
}
