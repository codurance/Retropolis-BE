package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.acceptance.cards.CommonCardStepDefinition.CARD_RESPONSE_ID;
import static com.codurance.retropolis.utils.HttpWrapper.executePatch;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import io.cucumber.java.en.When;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;

public class RemoveUpvoteStepDefinitionIntegrationTest extends BaseStepDefinition {

  public RemoveUpvoteStepDefinitionIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @When("the user removes card vote with voter:{string}")
  public void theUserRemovesCardVoteWithVoter(String email) {
    executePatch(url + "/cards/" + CARD_RESPONSE_ID + "/vote",
        new HttpEntity<>(new UpVoteRequestObject(email, false)));
  }
}
