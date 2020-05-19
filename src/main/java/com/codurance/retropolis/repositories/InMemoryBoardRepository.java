package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.Column;
import java.util.ArrayList;
import java.util.List;
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
    board.getColumns().stream().filter(column -> card.getColumnId() == column.getId()).findAny().get().getCards().add(card);
  }
}
