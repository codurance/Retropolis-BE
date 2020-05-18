package com.codurance.retropolis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.repositories.BoardRepository;
import com.codurance.retropolis.repositories.InMemoryBoardRepository;
import com.codurance.retropolis.services.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardAcceptanceTest {

  private BoardService boardService;

  @BeforeEach
  void setUp() {
    BoardRepository boardRepository = new InMemoryBoardRepository();
    boardService = new BoardService(boardRepository);
  }

  @Test
  void should_return_board_with_three_static_columns() {
    Board board = boardService.getBoard();

    assertEquals(3, board.getColumns().size());
  }
}
