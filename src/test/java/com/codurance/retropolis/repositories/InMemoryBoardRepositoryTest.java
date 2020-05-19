package com.codurance.retropolis.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryBoardRepositoryTest {


  private BoardRepository boardRepository;

  @BeforeEach
  void setUp() {
    boardRepository = new InMemoryBoardRepository();
  }

  @Test
  void should_return_three_columns() {
    Board board = boardRepository.getBoard();
    assertEquals(3, board.getColumns().size());
  }

  @Test
  void should_return_three_columns_with_correct_titles() {
    Board board = boardRepository.getBoard();
    assertEquals("Start", board.getColumns().get(0).getTitle());
    assertEquals("Stop", board.getColumns().get(1).getTitle());
    assertEquals("Continue", board.getColumns().get(2).getTitle());
  }

  @Test
  void should_add_card_to_correct_column() {
    Board board = boardRepository.getBoard();
    int columnId = board.getColumns().get(0).getId();

    boardRepository.addCard(new Card("hello", 1, columnId));

    assertEquals(1, board.getColumns().get(0).getCards().size());
  }

}