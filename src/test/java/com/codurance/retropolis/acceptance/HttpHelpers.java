package com.codurance.retropolis.acceptance;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpHelpers {

  public static ResponseResults getResponse = null;
  public static ResponseResults postResponse = null;

  private static final RestTemplate restTemplate = new RestTemplate();

  public static void executeGet(String url) {
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    getResponse = new ResponseResults(response.getBody(), response);
  }

  public static void executePost(String url, HttpEntity<?> request) {
    ResponseEntity<String> response =
        restTemplate.postForEntity(url, request, String.class);
    postResponse = new ResponseResults(response.getBody(), response);
  }
}
