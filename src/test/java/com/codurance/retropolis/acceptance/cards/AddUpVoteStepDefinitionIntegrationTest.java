package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePatch;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import com.codurance.retropolis.utils.HttpWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;


public class AddUpVoteStepDefinitionIntegrationTest extends BaseStepDefinition {

  public AddUpVoteStepDefinitionIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @Before
  public void cleanUpDatabase() throws SQLException {
    cleanUp();
  }

  @And("the client updates cards vote with this id in path and voter:{string} and addVote:{string} in body")
  public void theClientUpdatesToCardsWithThisIdInPathAndAddUpVoteInBody(String username, String addVote)
      throws JsonProcessingException {
    Card card = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });
    executePatch(url + "/cards/" + card.getId() + "/vote",
        new HttpEntity<>(new UpVoteRequestObject(username)));
  }

  @Then("the client receives a status code of {int} after update")
  public void theClientReceivesAStatusCodeOf(int statusCode) {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(statusCode));
  }

  @And("the client receives the card with the voter:{string}")
  public void theClientReceivesTheCardWithTheVoter(String username) throws JsonProcessingException {
    Card card = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    assertThat(card.getVoters().size(), is(1));
  }
}
