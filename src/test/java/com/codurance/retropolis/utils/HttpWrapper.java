package com.codurance.retropolis.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class HttpWrapper {

  public static ResponseResults responseResult = null;

  private static final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

  public static void executeGet(String url) {
    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
    responseResult = new ResponseResults(response.getBody(), response);
  }

  public static void executePost(String url, HttpEntity<?> request) {
    ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
    responseResult = new ResponseResults(response.getBody(), response);
  }

  public static void executePatch(String url, HttpEntity<?> request) {
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PATCH, request, String.class);
    responseResult = new ResponseResults(response.getBody(), response);
  }

  public static void executeDelete(String url) {
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    responseResult = new ResponseResults(response.getBody(), response);
  }

  public static class ResponseResults {

    private final ResponseEntity<String> theResponse;
    private final String body;

    ResponseResults(String body, final ResponseEntity<String> response) {
      this.theResponse = response;
      this.body = body;
    }

    public int getResponseCode() {
      return theResponse.getStatusCode().value();
    }

    public String getBody() {
      return body;
    }
  }
}
