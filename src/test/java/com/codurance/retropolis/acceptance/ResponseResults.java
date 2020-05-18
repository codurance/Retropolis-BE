package com.codurance.retropolis.acceptance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.springframework.http.client.ClientHttpResponse;

public class ResponseResults {

  private final ClientHttpResponse theResponse;
  private final String body;

  ResponseResults(final ClientHttpResponse response) throws IOException {
    this.theResponse = response;
    final InputStream bodyInputStream = response.getBody();
    this.body = new BufferedReader(new InputStreamReader(bodyInputStream, StandardCharsets.UTF_8)).lines()
        .collect(Collectors.joining("\n"));
  }

  public ClientHttpResponse getTheResponse() {
    return theResponse;
  }

  public String getBody() {
    return body;
  }
}
