package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePost;
import static com.codurance.retropolis.utils.HttpWrapper.postResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

public class CardStepDefsIntegrationTest {

  @When("the client posts to cards endpoint with column_id:{long}, text:{string} and userName:{string}")
  public void theClientPostsToCardsEndpointWithColumn_idAndText(Long columnId, String text, String userName) {
    executePost("http://localhost:5000/cards", new HttpEntity<>(new NewCardRequestObject(text, columnId, userName)));
  }

  @Then("^the client receives a status code of (\\d+)$")
  public void theClientReceivesStatusCodeOf(int statusCode) {
    final HttpStatus currentStatusCode = postResponse.getTheResponse().getStatusCode();
    assertThat(currentStatusCode.value(), is(statusCode));
  }

  @And("the client receives the card with the column_id:{long}, text:{string} and userName:{string}")
  public void theClientReceivesTheCardWithTheColumn_idAndText(Long columnId, String text, String userName)
      throws JsonProcessingException {
    Card card = new ObjectMapper().readValue(postResponse.getBody(), new TypeReference<>() {
    });

    assertThat(card.getColumnId(), is(columnId));
    assertThat(card.getText(), is(text));
    assertThat(card.getUserName(), is(userName));
  }
}
