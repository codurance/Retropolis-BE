package com.codurance.retropolis.acceptance;

import io.cucumber.java.Before;
import java.sql.SQLException;
import javax.sql.DataSource;

public class CommonStepDefinitions extends BaseStepDefinition {

  public CommonStepDefinitions(DataSource dataSource) {
    super(dataSource);
  }

  @Before
  public void cleanUpDatabase() throws SQLException {
    cleanUp();
  }
}
