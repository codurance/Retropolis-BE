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
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.http.HttpStatus;

public class BoardStepDefsIntegrationTest extends BaseStepDefinition {

  private final int TEST_BOARD_ID = 1;

  public BoardStepDefsIntegrationTest(DataSource dataSource) {
    super(dataSource);
  }

  @When("the client requests the test board")
  public void theClientCallsBoard() {
    HttpWrapper.executeGet(url + "/boards/" + TEST_BOARD_ID, headers);
  }

  @And("^the client receives board with three columns, \"([^\"]*)\", \"([^\"]*)\", and \"([^\"]*)\"$")
  public void theClientReceivesBoardWithThreeColumnsAnd(String firstTitle, String secondTitle,
      String thirdTitle) {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));
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
    List<Board> boards = new ObjectMapper()
        .readValue(responseResult.getBody(), new TypeReference<>() {
        });

    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));
    assertThat(boards.size(), is(1));
    assertThat(boards.get(0).getTitle(), is(title));
  }

  @Then("the user receives a empty list of boards")
  public void theUserReceivesAEmptyList() throws JsonProcessingException {
    List<Board> boards = new ObjectMapper()
        .readValue(responseResult.getBody(), new TypeReference<>() {
        });

    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));
    assertTrue(boards.isEmpty());
  }
}
