package com.codurance.retropolis.acceptance.board;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Column;
import com.codurance.retropolis.utils.HttpWrapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;

public class BoardStepDefsIntegrationTest {

  @When("^the client calls /board$")
  public void theClientCallsBoard() {
    HttpWrapper.executeGet("http://localhost:5000/board");
  }

  @Then("^the client receives status code of (\\d+)$")
  public void theClientReceivesStatusCodeOf(int statusCode) {
    final HttpStatus currentStatusCode = HttpWrapper.getResponse.getTheResponse().getStatusCode();
    assertThat(currentStatusCode.value(), is(statusCode));
  }

  @And("^the client receives board with three columns, \"([^\"]*)\", \"([^\"]*)\", and \"([^\"]*)\"$")
  public void theClientReceivesBoardWithThreeColumnsAnd(String firstTitle, String secondTitle, String thirdTitle) {
    assertThat(HttpWrapper.getResponse.getBody(),
        is(asJsonString(new Board(List.of(
            new Column(0, firstTitle, Collections.emptyList()),
            new Column(1, secondTitle, Collections.emptyList()),
            new Column(2, thirdTitle, Collections.emptyList()))))));
  }
}