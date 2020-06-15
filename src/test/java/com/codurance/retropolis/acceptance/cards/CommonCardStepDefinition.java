package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePatch;
import static com.codurance.retropolis.utils.HttpWrapper.executePost;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import com.codurance.retropolis.responses.CardResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;

public class CommonCardStepDefinition extends BaseStepDefinition {

  private final String CARD_TEXT = "Hello";
  private final Long COLUMN_ID = 1L;
  private final String USER_EMAIL = "john.doe@codurance.com";

  public CommonCardStepDefinition(DataSource dataSource) {
    super(dataSource);
  }

  @Given("the card exists")
  public void theCardExists() {
    executePost(url + "/cards",
        new HttpEntity<>(new NewCardRequestObject(CARD_TEXT, COLUMN_ID, USER_EMAIL), headers));
  }

  @When("the client adds card vote with voter:{string}")
  public void theClientAddsCardVoteWithVoter(String email)
      throws JsonProcessingException {
    CardResponseObject cardResponseObject = new ObjectMapper()
        .readValue(responseResult.getBody(), new TypeReference<>() {
        });
    executePatch(url + "/cards/" + cardResponseObject.getId() + "/vote",
        new HttpEntity<>(new UpVoteRequestObject(email, true)));
  }
}
