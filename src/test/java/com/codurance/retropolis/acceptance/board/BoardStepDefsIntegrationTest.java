package com.codurance.retropolis.acceptance.board;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Column;
import com.codurance.retropolis.utils.HttpWrapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;

public class BoardStepDefsIntegrationTest extends BaseStepDefinition {

  public BoardStepDefsIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @Before
  public void cleanUpDatabase() throws SQLException {
    cleanUp();
  }

  @When("^the client calls /boards/(\\d+)")
  public void theClientCallsBoard(int boardId) {
    HttpWrapper.executeGet("http://localhost:5000/boards/" + boardId);
  }

  @Then("^the client receives status code of (\\d+)$")
  public void theClientReceivesStatusCodeOf(int statusCode) {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(statusCode));
  }

  @And("^the client receives board with three columns, \"([^\"]*)\", \"([^\"]*)\", and \"([^\"]*)\"$")
  public void theClientReceivesBoardWithThreeColumnsAnd(String firstTitle, String secondTitle, String thirdTitle) {
    assertThat(HttpWrapper.responseResult.getBody(),
        is(asJsonString(new Board(1L, "test board", List.of(
            new Column(1L, firstTitle, Collections.emptyList()),
            new Column(2L, secondTitle, Collections.emptyList()),
            new Column(3L, thirdTitle, Collections.emptyList()))))));
  }

}
