package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.Column;
import com.codurance.retropolis.repositories.BoardRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {


  @Mock
  private BoardRepository boardRepository;
  private BoardService boardService;

  @BeforeEach
  void setUp() {
    boardService = new BoardService(boardRepository);
  }

  @Test
  void should_return_a_board() {
    when(boardRepository.getBoard(1)).thenReturn(
        new Board(List.of(new Column(1, "Start", Collections.emptyList()))));

    Board board = boardService.getBoard(1);

    verify(boardRepository).getBoard(1);
    assertEquals(1, board.getColumns().get(0).getId());
    assertEquals("Start", board.getColumns().get(0).getTitle());
    assertEquals(0, board.getColumns().get(0).getCards().size());
  }

  @Test
  public void should_add_card_to_in_memory_and_return_new_card() {
    Card cardToBeAdded = new Card(1, "hello", 1, "John Doe");
    Card card = boardService.addCard(cardToBeAdded);

    verify(boardRepository).addCard(card);
    assertEquals(card.getText(), cardToBeAdded.getText());
    assertEquals(card.getId(), cardToBeAdded.getId());
    assertEquals(card.getColumnId(), cardToBeAdded.getColumnId());
    assertEquals(card.getUserName(), cardToBeAdded.getUserName());
  }

}
