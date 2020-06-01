package com.codurance.retropolis.services;

import static java.util.Collections.emptyList;
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


  public static final String BOARD_TITLE = "test board";
  public static final long BOARD_ID = 1L;
  @Mock
  private BoardRepository boardRepository;
  private BoardService boardService;

  @BeforeEach
  void setUp() {
    boardService = new BoardService(boardRepository);
  }

  @Test
  void should_return_a_board() {
    String columnTitle = "Start";
    when(boardRepository.getBoard(BOARD_ID)).thenReturn(
        new Board(BOARD_ID, BOARD_TITLE, List.of(new Column(1L, columnTitle, emptyList()))));

    Board board = boardService.getBoard(BOARD_ID);

    verify(boardRepository).getBoard(BOARD_ID);
    assertEquals(BOARD_ID, board.getColumns().get(0).getId());
    assertEquals(columnTitle, board.getColumns().get(0).getTitle());
    assertEquals(0, board.getColumns().get(0).getCards().size());
  }

  @Test
  void should_return_boards_for_a_user() {
    long userId = 1L;
    when(boardRepository.getUsersBoards(userId)).thenReturn(List.of(
        new Board(BOARD_ID, BOARD_TITLE, Collections.emptyList())));

    List<Board> boards = boardService.getUsersBoards(userId);

    assertEquals(1, boards.size());
    assertEquals(BOARD_ID, boards.get(0).getId());
    assertEquals(BOARD_TITLE, boards.get(0).getTitle());
  }

}
