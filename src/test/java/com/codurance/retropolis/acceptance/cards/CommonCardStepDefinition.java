package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePatch;
import static com.codurance.retropolis.utils.HttpWrapper.executePost;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.utils.HttpWrapper;
import com.codurance.retropolis.web.requests.NewCardRequestObject;
import com.codurance.retropolis.web.requests.UpVoteRequestObject;
import com.codurance.retropolis.web.responses.CardResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

public class CommonCardStepDefinition extends BaseStepDefinition {

  public static Long CARD_RESPONSE_ID;
  private final String CARD_TEXT = "Hello";
  private final Long COLUMN_ID = 1L;
  private final String USER_EMAIL = "john.doe@codurance.com";

  public CommonCardStepDefinition(DataSource dataSource) {
    super(dataSource);
  }

  @Given("the card exists")
  public void theCardExists() throws JsonProcessingException {
    executePost(url + "/cards", new HttpEntity<>(new NewCardRequestObject(CARD_TEXT, COLUMN_ID, USER_EMAIL), headers));
    CardResponseObject cardResponseObject = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });
    CARD_RESPONSE_ID = cardResponseObject.getId();
  }

  @When("the user adds card vote with voter:{string}")
  public void theUserAddsCardVoteWithVoter(String email) {
    executePatch(url + "/cards/" + CARD_RESPONSE_ID + "/vote",
        new HttpEntity<>(new UpVoteRequestObject(email, true)));
  }

  @Then("the user receives an ok response")
  public void theUserReceivesAnOkResponse() {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));
  }
}
