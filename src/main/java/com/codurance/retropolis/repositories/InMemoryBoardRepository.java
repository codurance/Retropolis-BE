package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Column;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBoardRepository implements BoardRepository {

  private Board board;

  public InMemoryBoardRepository() {
    this.board = new Board(List.of(
        new Column(0, "Start", Collections.emptyList()),
        new Column(1, "Stop", Collections.emptyList()),
        new Column(2, "Continue", Collections.emptyList())));

  }

  @Override
  public Board getBoard() {
    return board;
  }
}
