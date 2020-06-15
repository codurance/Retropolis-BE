package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.responses.CardResponseObject;
import com.codurance.retropolis.utils.HttpWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import javax.sql.DataSource;
import org.springframework.http.HttpStatus;


public class AddUpVoteStepDefinitionIntegrationTest extends BaseStepDefinition {

  public AddUpVoteStepDefinitionIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @And("the user receives the card with their vote")
  public void theUserReceivesTheCardWithTheVoter() throws JsonProcessingException {
    CardResponseObject cardResponseObject = new ObjectMapper()
        .readValue(responseResult.getBody(), new TypeReference<>() {
        });

    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));
    assertThat(cardResponseObject.getTotalVoters(), is(1));
    assertThat(cardResponseObject.getHaveVoted(), is(true));
  }
}
