package com.codurance.retropolis.acceptance.board;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.entities.ColumnType;
import com.codurance.retropolis.utils.HttpWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class BoardStepDefsIntegrationTest extends BaseStepDefinition {

  private final String TOKEN = "token";
  private final int TEST_BOARD_ID = 1;
  private HttpHeaders headers;

  public BoardStepDefsIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @Before
  public void cleanUpDatabase() throws SQLException {
    cleanUp();
    headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, TOKEN);
  }

  @When("^the client calls /boards/(\\d+)")
  public void theClientCallsBoard(int boardId) {
    HttpWrapper.executeGet(url + "/boards/" + boardId, headers);
  }

  @Then("^the client receives status code of (\\d+)$")
  public void theClientReceivesStatusCodeOf(int statusCode) {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(statusCode));
  }

  @And("^the client receives board with three columns, \"([^\"]*)\", \"([^\"]*)\", and \"([^\"]*)\"$")
  public void theClientReceivesBoardWithThreeColumnsAnd(String firstTitle, String secondTitle, String thirdTitle) {
    assertThat(HttpWrapper.responseResult.getBody(),
        is(asJsonString(new Board(1L, "test board", List.of(
            new Column(1L, ColumnType.of(firstTitle)),
            new Column(2L, ColumnType.of(secondTitle)),
            new Column(3L, ColumnType.of(thirdTitle)))))));
  }

  @Given("a user has accessed the test board")
  public void aUserHasAccessedTheTestBoard() {
    HttpWrapper.executeGet(url + "/boards/" + TEST_BOARD_ID, headers);
  }

  @When("the user requests all their boards")
  public void theUserRequestsAllTheirBoards() {
    HttpWrapper.executeGet(url + "/boards", headers);
  }

  @Then("the user receives a list of the boards with one called {string}")
  public void theUserReceivesAListOfTheBoardsWithOneCalled(String title)
      throws JsonProcessingException {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));

    List<Board> boards = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    assertThat(boards.size(), is(1));
    assertThat(boards.get(0).getTitle(), is(title));
  }

  @Then("the user receives a empty list of boards")
  public void theUserReceivesAEmptyList() throws JsonProcessingException {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));

    List<Board> boards = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    assertTrue(boards.isEmpty());
  }
}
