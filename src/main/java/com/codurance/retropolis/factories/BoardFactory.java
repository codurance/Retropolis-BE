package com.codurance.retropolis.factories;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.entities.ColumnType;
import com.codurance.retropolis.web.requests.NewBoardRequestObject;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BoardFactory {

  public Board create(NewBoardRequestObject requestObject) {
    return new Board(requestObject.getTitle(), List.of(
        new Column(ColumnType.START),
        new Column(ColumnType.STOP),
        new Column(ColumnType.CONTINUE)
    ));
  }
}
