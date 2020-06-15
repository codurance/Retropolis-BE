package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executeDelete;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.responses.CardResponseObject;
import com.codurance.retropolis.utils.HttpWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javax.sql.DataSource;


public class DeleteCardStepDefinitionIntegrationTest extends BaseStepDefinition {

  public DeleteCardStepDefinitionIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @When("the client deletes to cards with this id passing it as path variable to endpoint")
  public void theClientDeletesToCardsEndpointWithPathVariable() throws JsonProcessingException {
    CardResponseObject cardResponseObject = new ObjectMapper()
        .readValue(responseResult.getBody(), new TypeReference<>() {
        });
    executeDelete(url + "/cards/" + cardResponseObject.getId());
  }

  @Then("the client receives a status code of {int} after card was deleted")
  public void theClientReceivesAStatusCodeOfAfterCardWasDeleted(int statusCode) {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(statusCode));
  }
}
