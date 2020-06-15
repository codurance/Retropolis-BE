package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executeDelete;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.web.responses.CardResponseObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.When;
import javax.sql.DataSource;


public class DeleteCardStepDefinitionIntegrationTest extends BaseStepDefinition {

  public DeleteCardStepDefinitionIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @When("the user deletes existing card")
  public void theUserDeletesExistingCard() throws JsonProcessingException {
    CardResponseObject cardResponseObject = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });
    executeDelete(url + "/cards/" + cardResponseObject.getId());
  }
}
