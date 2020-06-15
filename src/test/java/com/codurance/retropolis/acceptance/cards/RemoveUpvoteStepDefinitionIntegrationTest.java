package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePatch;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import com.codurance.retropolis.responses.CardResponseObject;
import com.codurance.retropolis.utils.HttpWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

public class RemoveUpvoteStepDefinitionIntegrationTest extends BaseStepDefinition {

  public RemoveUpvoteStepDefinitionIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @When("the user removes card vote with voter:{string}")
  public void theUserRemovesCardVoteWithVoter(String email)
      throws JsonProcessingException {
    CardResponseObject cardResponseObject = new ObjectMapper()
        .readValue(responseResult.getBody(), new TypeReference<>() {
        });
    executePatch(url + "/cards/" + cardResponseObject.getId() + "/vote",
        new HttpEntity<>(new UpVoteRequestObject(email, false)));
  }

  @Then("the user receives the card without their vote")
  public void theUserReceivesTheCardWithoutTheirVote() throws JsonProcessingException {
    CardResponseObject cardResponseObject = new ObjectMapper()
        .readValue(responseResult.getBody(), new TypeReference<>() {
        });

    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));
    assertThat(cardResponseObject.getTotalVoters(), is(0));
    assertThat(cardResponseObject.getHaveVoted(), is(false));
  }
}
