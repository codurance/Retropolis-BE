package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.entities.ColumnType;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.factories.BoardFactory;
import com.codurance.retropolis.repositories.BoardRepository;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

  public static final Long COLUMN_ID = 1L;
  public static final Long USER_ID = 1L;
  public static final Long BOARD_ID = 1L;
  public static final String USER_EMAIL = "john.doe@codurance.com";
  public static final String BOARD_TITLE = "test board";

  @Mock
  private BoardRepository boardRepository;

  @Mock
  private BoardFactory boardFactory;

  @Mock
  private UserService userService;

  private BoardService boardService;

  @BeforeEach
  void setUp() {
    boardService = new BoardService(boardRepository, boardFactory, userService);
  }

  @Test
  void returns_a_board() {
    String columnTitle = "Start";
    when(boardRepository.getBoard(BOARD_ID)).thenReturn(
        new Board(BOARD_ID, BOARD_TITLE, List.of(new Column(COLUMN_ID, ColumnType.START))));

    Board board = boardService.getBoard(USER_EMAIL, BOARD_ID);

    verify(boardRepository).getBoard(BOARD_ID);
    assertEquals(BOARD_ID, board.getColumns().get(0).getId());
    assertEquals(columnTitle, board.getColumns().get(0).getTitle());
    assertEquals(0, board.getColumns().get(0).getCards().size());
  }

  @Test
  void creates_a_board() {
    NewBoardRequestObject requestObject = new NewBoardRequestObject(BOARD_TITLE, USER_EMAIL);
    Board board = new Board(BOARD_ID, BOARD_TITLE, Collections.emptyList());

    when(boardFactory.create(requestObject)).thenReturn(board);
    when(boardRepository.insert(board)).thenReturn(board);

    boardService.createBoard(requestObject);

    verify(boardFactory).create(requestObject);
    verify(boardRepository).insert(board);
    verify(userService).registerUserIfNotExists(USER_EMAIL, board.getId());
  }

  @Test
  void should_return_boards_for_a_user() {
    when(boardRepository.getUsersBoards(USER_ID)).thenReturn(List.of(
        new Board(BOARD_ID, BOARD_TITLE, Collections.emptyList())));
    when(userService.findOrCreateBy(USER_EMAIL)).thenReturn(new User(USER_ID, USER_EMAIL));

    List<Board> boards = boardService.getUsersBoards(USER_EMAIL);

    assertEquals(1, boards.size());
    assertEquals(BOARD_ID, boards.get(0).getId());
    assertEquals(BOARD_TITLE, boards.get(0).getTitle());
  }

  @Test
  public void should_throw_BoardNotFoundException_when_boardId_is_invalid_on_() {
    doThrow(new RuntimeException()).when(userService).registerUserIfNotExists(USER_EMAIL, BOARD_ID);
    assertThrows(BoardNotFoundException.class, () -> boardService.getBoard(USER_EMAIL, BOARD_ID));
  }

}
