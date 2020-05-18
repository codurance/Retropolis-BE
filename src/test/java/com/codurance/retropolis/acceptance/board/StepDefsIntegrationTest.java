package com.codurance.retropolis.acceptance.board;

import static com.codurance.retropolis.utils.Utils.asJsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.SpringIntegrationTest;
import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Column;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;

public class StepDefsIntegrationTest extends SpringIntegrationTest {


  @When("^the client calls /board$")
  public void theClientCallsBoard() {
    executeGet("http://localhost:5000/board");
  }

  @Then("^the client receives status code of (\\d+)$")
  public void theClientReceivesStatusCodeOf(int statusCode) throws IOException {
    final HttpStatus currentStatusCode = latestResponse.getTheResponse().getStatusCode();
    assertThat("status code is incorrect : " + latestResponse.getBody(), currentStatusCode.value(), is(statusCode));
  }

  @And("^the client receives board with three columns, \"([^\"]*)\", \"([^\"]*)\", and \"([^\"]*)\"$")
  public void theClientReceivesBoardWithThreeColumnsAnd(String firstTitle, String secondTitle, String thirdTitle) {
    assertThat(latestResponse.getBody(),
        is(asJsonString(new Board(List.of(
            new Column(0, firstTitle, Collections.emptyList()),
            new Column(1, secondTitle, Collections.emptyList()),
            new Column(2, thirdTitle, Collections.emptyList()))))));
  }
}
