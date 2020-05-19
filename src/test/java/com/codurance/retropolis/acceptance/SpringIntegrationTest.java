package com.codurance.retropolis.acceptance;

import com.codurance.retropolis.Retropolis;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = Retropolis.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringIntegrationTest {

  public static ResponseResults latestResponse = null;

  private final RestTemplate restTemplate = new RestTemplate();

  public void executeGet(String url) {
    latestResponse = restTemplate.execute(url, HttpMethod.GET, null, ResponseResults::new);
  }

}
