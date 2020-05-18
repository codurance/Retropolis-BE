package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Column;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BoardRepository {

  private Board board;

  public BoardRepository() {
    this.board = new Board(List.of(
        new Column(0, "Start", Collections.emptyList()),
        new Column(1, "Stop", Collections.emptyList()),
        new Column(2, "Continue", Collections.emptyList())));

  }

  public Board getBoard() {
    return board;
  }
}
