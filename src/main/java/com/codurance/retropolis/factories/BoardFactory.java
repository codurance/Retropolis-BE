package com.codurance.retropolis.factories;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import org.springframework.stereotype.Component;

@Component
public class BoardFactory {

  public Board create(NewBoardRequestObject requestObject) {
    throw new UnsupportedOperationException("Implement me!");
  }
}
