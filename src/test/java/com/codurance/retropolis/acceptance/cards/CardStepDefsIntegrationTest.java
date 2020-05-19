package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.Utils.asJsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.HttpHelpers;
import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.springframework.http.HttpEntity;

public class CardStepDefsIntegrationTest {

  @When("the client posts to cards endpoint with column_id:{int} and text:{string}")
  public void theClientPostsToCardsEndpointWithColumn_idAndText(int columnId, String text) {
    Card card = new CardFactory().create(new NewCardRequestObject(text, columnId));
    HttpHelpers.executePost("http://localhost:5000/cards", new HttpEntity<>(card), Card.class);
  }

  @And("the client receives the card with the column_id:{int} and text:{string}")
  public void theClientReceivesTheCardWithTheColumn_idAndText(int columnId, String text) {
    assertThat(HttpHelpers.postResponse.getBody(),
        is(asJsonString(new CardFactory().create(new NewCardRequestObject(text, columnId)))));

  }
}
