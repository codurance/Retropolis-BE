package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePatch;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.utils.HttpWrapper;
import com.codurance.retropolis.web.requests.UpdateCardRequestObject;
import com.codurance.retropolis.web.responses.CardResponseObject;
import com.codurance.retropolis.web.responses.CardUpdatedTextResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;


public class UpdateCardStepDefinitionIntegrationTest extends BaseStepDefinition {

  public UpdateCardStepDefinitionIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @When("the user updates the existing card with text {string}")
  public void theUserUpdatesTheExistingCardWithText(String newText)
      throws JsonProcessingException {
    CardResponseObject cardResponseObject = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    executePatch(url + "/cards/" + cardResponseObject.getId(),
        new HttpEntity<>(new UpdateCardRequestObject(newText)));
  }

  @Then("the user receives the card with the text:{string}")
  public void theUserReceivesTheCardWithTheText(String newText) throws JsonProcessingException {
    CardUpdatedTextResponseObject card = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));
    assertThat(card.getText(), is(newText));
  }
}
