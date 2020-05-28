package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.deleteResponse;
import static com.codurance.retropolis.utils.HttpWrapper.executeDelete;
import static com.codurance.retropolis.utils.HttpWrapper.executePatch;
import static com.codurance.retropolis.utils.HttpWrapper.executePost;
import static com.codurance.retropolis.utils.HttpWrapper.postResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpVoteRequestObject;
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
import org.springframework.http.HttpStatus;


public class AddUpVoteStepDefinitionIntegrationTest extends BaseStepDefinition {

    public AddUpVoteStepDefinitionIntegrationTest(DataSource dataSource) {
        super(dataSource);
    }

    @Before
    public void cleanUpDatabase() throws SQLException {
        cleanUp();
    }

    @And("the client updates to cards with this id in path and addUpVote {string} in body")
    public void theClientUpdatesToCardsWithThisIdInPathAndAddUpVoteInBody(String username)
        throws JsonProcessingException {
        Card card = new ObjectMapper().readValue(postResponse.getBody(), new TypeReference<>() {
        });
        executePatch("http://localhost:5000/cards/" + card.getId(), new UpVoteRequestObject("username"));
    }

    @Then("the client receives a status code of 200 after card was updated")
    public void theClientReceivesAStatusCodeOf200AfterCardWasUpdated() throws JsonProcessingException {
        Card card = new ObjectMapper().readValue(postResponse.getBody(), new TypeReference<>() {
        });
        executeDelete("http://localhost:5000/cards/" + card.getId());
    }
}
