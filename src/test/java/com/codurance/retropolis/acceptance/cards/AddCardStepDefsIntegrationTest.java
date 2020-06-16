package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePost;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.web.requests.NewCardRequestObject;
import com.codurance.retropolis.web.responses.CardResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

public class AddCardStepDefsIntegrationTest extends BaseStepDefinition {

  private final Long columnId = 1L;

  public AddCardStepDefsIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @When("the user posts to cards endpoint with text:{string} and email:{string}")
  public void theUserPostsToCardsEndpointWithText(String text, String email) {
    executePost(url + "/cards", new HttpEntity<>(new NewCardRequestObject(text, columnId, email), headers));
  }

  @Then("the user receives the card with the text:{string} and author:{string}")
  public void theUserReceivesTheCardWithTheColumn_idTextAndAuthor(String text, String author) throws JsonProcessingException {
    CardResponseObject cardResponseObject = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    assertThat(responseResult.getResponseCode(), is(HttpStatus.CREATED.value()));
    assertThat(cardResponseObject.getText(), is(text));
    assertThat(cardResponseObject.getColumnId(), is(columnId));
    assertThat(cardResponseObject.getAuthor(), is(author));
  }
}
