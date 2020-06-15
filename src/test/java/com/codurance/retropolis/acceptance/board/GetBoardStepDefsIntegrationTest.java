package com.codurance.retropolis.acceptance.board;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.entities.ColumnType;
import com.codurance.retropolis.utils.HttpWrapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.http.HttpStatus;

public class GetBoardStepDefsIntegrationTest extends BaseStepDefinition {

  private final int TEST_BOARD_ID = 1;

  public GetBoardStepDefsIntegrationTest(DataSource dataSource) {
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
}
