package com.codurance.retropolis.acceptance.board;

import static com.codurance.retropolis.utils.HttpWrapper.responseResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.responses.UserBoardResponseObject;
import com.codurance.retropolis.utils.HttpWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.http.HttpStatus;

public class GetAllBoardsStepDefsIntegrationTest extends BaseStepDefinition {

  private final int TEST_BOARD_ID = 1;

  public GetAllBoardsStepDefsIntegrationTest(DataSource dataSource) {
    super(dataSource);
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
    List<UserBoardResponseObject> boards = new ObjectMapper()
        .readValue(responseResult.getBody(), new TypeReference<>() {
        });

    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));
    assertThat(boards.size(), is(1));
    assertThat(boards.get(0).getTitle(), is(title));
  }

  @Then("the user receives a empty list of boards")
  public void theUserReceivesAEmptyList() throws JsonProcessingException {
    List<UserBoardResponseObject> boards = new ObjectMapper()
        .readValue(responseResult.getBody(), new TypeReference<>() {
        });

    assertThat(HttpWrapper.responseResult.getResponseCode(), is(HttpStatus.OK.value()));
    assertTrue(boards.isEmpty());
  }
}
