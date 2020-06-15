package com.codurance.retropolis.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.web.requests.NewBoardRequestObject;
import org.junit.jupiter.api.Test;

public class BoardFactoryTest {

  @Test
  void returns_a_new_board() {
    BoardFactory boardFactory = new BoardFactory();
    String title = "new board";
    String email = "john.doe@codurance.com";
    NewBoardRequestObject newBoardRequestObject = new NewBoardRequestObject(title, email);

    Board board = boardFactory.create(newBoardRequestObject);
    assertEquals(title, board.getTitle());
    assertEquals(3, board.getColumns().size());
    assertEquals("Start", board.getColumns().get(0).getTitle());
    assertEquals("Stop", board.getColumns().get(1).getTitle());
    assertEquals("Continue", board.getColumns().get(2).getTitle());
  }
}
