package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePatch;
import static com.codurance.retropolis.utils.HttpWrapper.executePost;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpdateCardRequestObject;
import com.codurance.retropolis.utils.HttpWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;


public class UpdateCardStepDefinitionIntegrationTest extends BaseStepDefinition {

  public UpdateCardStepDefinitionIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @Before
  public void cleanUpDatabase() throws SQLException {
    cleanUp();
  }

  @When("the card exists with an id")
  public void theCardExistsWithId() {
    executePost("http://localhost:5000/cards", new HttpEntity<>(new NewCardRequestObject("Hello", 1L, "John Doe")));
  }

  @And("the client updates to cards with this id and changes the text to {string}")
  public void theClientUpdatesToCardsWithThisIdAndChangesTheTextFromTo(String newText) throws JsonProcessingException {
    Card card = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    executePatch("http://localhost:5000/cards/" + card.getId(), new HttpEntity<>(new UpdateCardRequestObject(newText)));
  }

  @And("the client receives the card with the text:{string}")
  public void theClientReceivesTheCardWithTheText(String newText) throws JsonProcessingException {
    Card card = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });
    assertThat(card.getText(), is(newText));
  }

  @Then("the client receives {int} status code")
  public void theClientReceivesStatusCode(int statusCode) {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(statusCode));
  }
}
