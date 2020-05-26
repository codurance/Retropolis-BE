package com.codurance.retropolis.repositories;

import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.Column;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryBoardRepository implements BoardRepository {

  private final Board board;

  public InMemoryBoardRepository() {
    this.board = new Board(1, "test board", List.of(
        new Column(1, "Start", new ArrayList<>()),
        new Column(2, "Stop", new ArrayList<>()),
        new Column(3, "Continue", new ArrayList<>())));

  }

  @Override
  public Board getBoard(Integer id) {
    return board;
  }

  @Override
  public void addCard(Card card) {
    Optional<Column> columnOptional = board.getColumns().stream()
        .filter(column -> card.getColumnId() == column.getId())
        .findAny();
    if (!columnOptional.isPresent()) {
      throw new ColumnNotFoundException("Column Id is not valid");
    }
    columnOptional.get().getCards().add(card);
  }

}
