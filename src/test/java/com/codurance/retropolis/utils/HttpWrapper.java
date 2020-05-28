package com.codurance.retropolis.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpWrapper {

  public static ResponseResults getResponse = null;
  public static ResponseResults postResponse = null;
  public static ResponseResults deleteResponse = null;
  public static ResponseResults patchResponse = null;

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

  public static void executeDelete(String url) {
    ResponseEntity<String> response =
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    deleteResponse = new ResponseResults(response.getBody(), response);
  }

  public static void executePatch(String url, HttpEntity<?> request) {
    ResponseEntity<String> response =
        restTemplate.patchForObject(url, request, );
    patchResponse = new ResponseResults(response.getBody(), response);
  }

  public static class ResponseResults {

    private final ResponseEntity<String> theResponse;
    private final String body;

    ResponseResults(String body, final ResponseEntity<String> response) {
      this.theResponse = response;
      this.body = body;
    }

    public ResponseEntity<String> getTheResponse() {
      return theResponse;
    }

    public String getBody() {
      return body;
    }
  }
}
