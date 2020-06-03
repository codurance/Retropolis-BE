package com.codurance.retropolis.factories;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.entities.ColumnType;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BoardFactory {

  public Board create(NewBoardRequestObject requestObject) {
    return new Board(requestObject.getTitle(), List.of(
        new Column(ColumnType.START, Collections.emptyList()),
        new Column(ColumnType.STOP, Collections.emptyList()),
        new Column(ColumnType.CONTINUE, Collections.emptyList())
    ));
  }
}
