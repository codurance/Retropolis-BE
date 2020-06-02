package com.codurance.retropolis.acceptance.board;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Column;
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
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.http.HttpHeaders;

public class BoardStepDefsIntegrationTest extends BaseStepDefinition {

  public static final String TOKEN = "token";

  public BoardStepDefsIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @Before
  public void cleanUpDatabase() throws SQLException {
    cleanUp();
  }

  @When("^the client calls /boards/(\\d+)")
  public void theClientCallsBoard(int boardId) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, TOKEN);
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
            new Column(1L, firstTitle, Collections.emptyList()),
            new Column(2L, secondTitle, Collections.emptyList()),
            new Column(3L, thirdTitle, Collections.emptyList()))))));
  }

  @Given("a user has accessed the test board")
  public void aUserHasAccessedTheTestBoard() {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, TOKEN);
    HttpWrapper.executeGet(url + "/boards/1", headers);
  }

  @And("a user has previously accessed the board")
  public void aUserHasPreviouslyAccessedTheBoard() {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, TOKEN);
    HttpWrapper.executeGet(url + "/boards/1", headers);
  }

  @When("the user requests all their boards")
  public void theUserRequestsAllTheirBoards() {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, TOKEN);
    HttpWrapper.executeGet(url + "/boards", headers);
  }

  @Then("the user receives a list of the boards with one called {string}")
  public void theUserReceivesAListOfTheBoardsWithOneCalled(String title)
      throws JsonProcessingException {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(200));

    List<Board> boards = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    assertThat(boards.size(), is(1));
    assertThat(boards.get(0).getTitle(), is("test board"));
  }

  @Then("the user receives a empty list of boards")
  public void theUserReceivesAEmptyList() throws JsonProcessingException {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(200));

    List<Board> boards = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    assertTrue(boards.isEmpty());
  }
}
