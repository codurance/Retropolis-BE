package com.codurance.retropolis.acceptance.cards;

import static com.codurance.retropolis.utils.HttpWrapper.executePost;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import com.codurance.retropolis.requests.NewCardRequestObject;
import io.cucumber.java.en.Given;
import javax.sql.DataSource;
import org.springframework.http.HttpEntity;

public class CommonCardStepDefinition extends BaseStepDefinition {

  private final String CARD_TEXT = "Hello";
  private final Long COLUMN_ID = 1L;
  private final String USER_EMAIL = "john.doe@codurance.com";

  public CommonCardStepDefinition(DataSource dataSource) {
    super(dataSource);
  }

  @Given("the card exists")
  public void theCardExists() {
    executePost(url + "/cards",
        new HttpEntity<>(new NewCardRequestObject(CARD_TEXT, COLUMN_ID, USER_EMAIL), headers));
  }
}
