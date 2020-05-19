package com.codurance.retropolis.repositories;

import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.Column;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBoardRepository implements BoardRepository {

  private final Board board;

  public InMemoryBoardRepository() {
    this.board = new Board(List.of(
        new Column(0, "Start", new ArrayList<>()),
        new Column(1, "Stop", new ArrayList<>()),
        new Column(2, "Continue", new ArrayList<>())));

  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public void addCard(Card card) {
    Optional<Column> columnOptional = board.getColumns().stream()
        .filter(column -> card.getColumnId() == column.getId())
        .findAny();
    if (!columnOptional.isPresent()) {
      throw new ColumnNotFoundException();
    }
    columnOptional.get().getCards().add(card);
  }
}
