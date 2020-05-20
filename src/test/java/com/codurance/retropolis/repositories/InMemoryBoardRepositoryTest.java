package com.codurance.retropolis.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryBoardRepositoryTest {


  private static final int NON_EXISTENT_COLUMN_ID = 999;
  private int FIXED_COLUMN_SIZE = 3;
  private BoardRepository boardRepository;

  @BeforeEach
  void setUp() {
    boardRepository = new InMemoryBoardRepository();
  }

  @Test
  void should_return_three_columns() {
    Board board = boardRepository.getBoard();
    assertEquals(FIXED_COLUMN_SIZE, board.getColumns().size());
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

    boardRepository.addCard(new Card(1, "hello", columnId, "John Doe"));

    assertEquals(1, board.getColumns().get(0).getCards().size());
  }

  @Test
  void should_throw_exception_when_columnId_is_invalid() {
    assertThrows(ColumnNotFoundException.class, () -> boardRepository.addCard(new Card(1, "hello", NON_EXISTENT_COLUMN_ID, "John Doe")));
  }

  @Test
  void should_add_card_with_correct_fields() {
    Board board = boardRepository.getBoard();
    int columnId = board.getColumns().get(0).getId();

    boardRepository.addCard(new Card(1, "hello", columnId, "John Doe"));

    Card card = board.getColumns().get(0).getCards().get(0);
    assertEquals(1, card.getId());
    assertEquals("hello", card.getText());
    assertEquals(0, card.getColumnId());
    assertEquals("John Doe", card.getUserName());
  }
}