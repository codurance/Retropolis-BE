package com.codurance.retropolis.acceptance;

import org.springframework.http.ResponseEntity;

public class ResponseResults {

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
