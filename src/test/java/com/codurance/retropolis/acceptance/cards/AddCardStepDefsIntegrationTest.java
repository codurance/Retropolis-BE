package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePost;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
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
import org.springframework.http.HttpHeaders;

public class AddCardStepDefsIntegrationTest extends BaseStepDefinition {

  private final String TOKEN = "token";
  private HttpHeaders headers;

  public AddCardStepDefsIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @Before
  public void cleanUpDatabase() throws SQLException {
    cleanUp();
    headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, TOKEN);
  }

  @When("the client posts to cards endpoint with column_id:{long}, text:{string} and email:{string}")
  public void theClientPostsToCardsEndpointWithColumn_idAndText(Long columnId, String text, String email) {
    executePost(url + "/cards", new HttpEntity<>(new NewCardRequestObject(text, columnId, email), headers));
  }

  @Then("^the client receives a status code of (\\d+)$")
  public void theClientReceivesStatusCodeOf(int statusCode) {
    assertThat(responseResult.getResponseCode(), is(statusCode));
  }

  @And("the client receives the card with the column_id:{long}, text:{string} and userId:{long}")
  public void theClientReceivesTheCardWithTheColumn_idAndText(Long columnId, String text, Long userId)
      throws JsonProcessingException {
    Card card = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    assertThat(card.getColumnId(), is(columnId));
    assertThat(card.getText(), is(text));
    assertThat(card.getUserId(), is(userId));
  }
}
