package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.Utils.asJsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.SpringIntegrationTest;
import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Column;
import com.codurance.retropolis.requests.NewCardRequestObject;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import java.util.Collections;
import java.util.List;

public class StepDefsIntegrationTest extends SpringIntegrationTest {

  @When("the client posts to cards endpoint with column_id:{int} and text:{string}")
  public void theClientPostsToCardsEndpointWithColumn_idAndText(int columnId, String text) {
    executePost("http://localhost:5000/cards", columnId, text);
  }

  @And("the client receives the card with the column_id:{int} and text:{string}")
  public void theClientReceivesTheCardWithTheColumn_idAndText(int columnId, String text) {
    assertThat(postResponse.getBody(),
        is(asJsonString(new CardFactory().create(new NewCardRequestObject(text, columnId)))));

  }
}
