package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executeDelete;
import static com.codurance.retropolis.utils.HttpWrapper.executePost;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.utils.HttpWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;


public class DeleteCardStepDefinitionIntegrationTest extends BaseStepDefinition {

  private final String USER_EMAIL = "john.doe@codurance.com";
  private final Long COLUMN_ID = 1L;
  private final String CARD_TEXT = "Hello";
  private final String TOKEN = "token";
  private HttpHeaders headers;


  public DeleteCardStepDefinitionIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @Before
  public void cleanUpDatabase() throws SQLException {
    cleanUp();
    headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, TOKEN);
  }

  @When("the card exists with id")
  public void theCardExistsWithId() {
    HttpEntity<NewCardRequestObject> request = new HttpEntity<>(new NewCardRequestObject(CARD_TEXT, COLUMN_ID, USER_EMAIL),
        headers);
    executePost(url + "/cards", request);
  }

  @When("the client deletes to cards with this id passing it as path variable to endpoint")
  public void theClientDeletesToCardsEndpointWithPathVariable() throws JsonProcessingException {
    Card card = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });
    executeDelete(url + "/cards/" + card.getId());
  }

  @Then("the client receives a status code of {int} after card was deleted")
  public void theClientReceivesAStatusCodeOfAfterCardWasDeleted(int statusCode) {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(statusCode));
  }
}
