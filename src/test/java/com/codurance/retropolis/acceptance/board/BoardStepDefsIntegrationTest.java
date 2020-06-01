package com.codurance.retropolis.acceptance.board;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static com.codurance.retropolis.utils.HttpWrapper.executePost;
import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.Column;
import com.codurance.retropolis.requests.NewCardRequestObject;
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
import org.springframework.http.HttpEntity;

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
    HttpWrapper.executeGet(url + "/boards/" + boardId);
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

  @Given("a board exists called {string}")
  public void aBoardExistsCalled(String title) {
    // TODO post a real board when we have the endpoint for it
  }

  @Given("a user has previously accessed {string}")
  public void aUserHasPreviouslyAccessed(String title) {
    // TODO make a request to create a connection between user and board
  }

  @When("the user requests all their boards")
  public void theUserRequestsAllTheirBoards() {
    HttpWrapper.executeGet(url + "/boards");
  }

  @Then("the user receives a list of the boards with one called {string}")
  public void theUserReceivesAListOfTheBoardsWithOneCalled(String title)
      throws JsonProcessingException {
    assertThat(HttpWrapper.responseResult.getResponseCode(), is(200));

    List<Board> boards = new ObjectMapper().readValue(responseResult.getBody(), new TypeReference<>() {
    });

    assertThat(boards.size(), is(1));
    // TODO assert title by given title when we are able to post a board with title
    assertThat(boards.get(0).getTitle(), is("test board"));
  }
}
