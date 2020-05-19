package com.codurance.retropolis.acceptance;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpHelpers {

  public static ResponseResults getResponse = null;
  public static ResponseEntity<?> postResponse = null;

  private static final RestTemplate restTemplate = new RestTemplate();

  public static void executeGet(String url) {
    getResponse = restTemplate.execute(url, HttpMethod.GET, null, ResponseResults::new);
  }

  public static void executePost(String url, HttpEntity<?> request, Class<?> responseType) {
    postResponse = restTemplate
        .exchange(url, HttpMethod.POST, request, responseType);
  }
}
