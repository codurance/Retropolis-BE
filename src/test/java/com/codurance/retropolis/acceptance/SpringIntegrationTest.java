package com.codurance.retropolis.acceptance;

import com.codurance.retropolis.Retropolis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = Retropolis.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ContextConfiguration
public class SpringIntegrationTest {

  public ResponseResults latestResponse;

  @Autowired
  private RestTemplate restTemplate;

  public void executeGet(String url) {
    latestResponse = restTemplate.execute(url, HttpMethod.GET, null, ResponseResults::new);
  }

}
