package com.codurance.retropolis.acceptance.cards;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.codurance.retropolis.utils.HttpWrapper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class DeleteCardStepDefinitionIntegrationTest extends BaseStepDefinition {

    public DeleteCardStepDefinitionIntegrationTest(DataSource dataSource) {
        super(dataSource);
    }

    @Before
    public void cleanUpDatabase() throws SQLException {
        cleanUp();
    }

    @When("the card exists with id")
    public void theCardExistsWithId() {
        executePost(url + "/cards", new HttpEntity<>(new NewCardRequestObject("Hello", 1L, "John Doe")));
    }

    @When("the client deletes to cards with this id passing it as path variable to endpoint")
    public void theClientDeletesToCardsEndpointWithPathVariable() throws JsonProcessingException {
        Card card = new ObjectMapper().readValue(postResponse.getBody(), new TypeReference<>() {
        });
        executeDelete(url + "/cards/" + card.getId());
    }

    @Then("the client receives a status code of {int} after card was deleted")
    public void theClientReceivesAStatusCodeOfAfterCardWasDeleted(int statusCode) {
        final HttpStatus currentStatusCode = deleteResponse.getTheResponse().getStatusCode();
        assertThat(currentStatusCode.value(), is(statusCode));
    }
}
