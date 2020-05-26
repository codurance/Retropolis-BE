package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.models.Board;
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
        new Board(1L, "test board", List.of(new Column(1L, "Start", Collections.emptyList()))));

    Board board = boardService.getBoard(1);

    verify(boardRepository).getBoard(1);
    assertEquals(1, board.getColumns().get(0).getId());
    assertEquals("Start", board.getColumns().get(0).getTitle());
    assertEquals(0, board.getColumns().get(0).getCards().size());
  }

}
