package com.codurance.retropolis.acceptance;

import com.codurance.retropolis.Retropolis;
import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = Retropolis.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringIntegrationTest {

  public static ResponseResults getResponse = null;
  public static ResponseEntity<?> postResponse = null;

  private final RestTemplate restTemplate = new RestTemplate();

  public void executeGet(String url) {
    getResponse = restTemplate.execute(url, HttpMethod.GET, null, ResponseResults::new);
  }

  public void executePost(String url, int columnId, String text) {
    RestTemplate restTemplate = new RestTemplate();
    CardFactory cardFactory = new CardFactory();
    Card card = cardFactory.create(new NewCardRequestObject(text, columnId));
    HttpEntity<Card> request = new HttpEntity<>(card);
    postResponse = restTemplate
        .exchange(url, HttpMethod.POST, request, Card.class);
  }

}
