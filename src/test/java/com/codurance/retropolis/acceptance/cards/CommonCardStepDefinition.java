package com.codurance.retropolis.acceptance.cards;

import com.codurance.retropolis.acceptance.BaseStepDefinition;
import javax.sql.DataSource;

public class CommonCardStepDefinition extends BaseStepDefinition {

  public CommonCardStepDefinition(DataSource dataSource) {
    super(dataSource);
  }
}
