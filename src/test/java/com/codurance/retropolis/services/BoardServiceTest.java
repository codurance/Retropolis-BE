package com.codurance.retropolis.services;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.factories.BoardFactory;
import com.codurance.retropolis.repositories.BoardRepository;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import com.codurance.retropolis.responses.BoardResponseObject;
import com.codurance.retropolis.responses.BoardResponseObjectFactory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

  private final Long USER_ID = 1L;
  private final Long BOARD_ID = 1L;
  private final User USER = new User("john.doe@codurance.com", "John Doe");
  private final String BOARD_TITLE = "test board";

  @Mock
  private BoardRepository boardRepository;

  @Mock
  private BoardFactory boardFactory;

  @Mock
  private UserService userService;

  @Mock
  private BoardResponseObjectFactory boardResponseFactory;

  private BoardService boardService;

  @BeforeEach
  void setUp() {
    boardService = new BoardService(boardRepository, boardFactory, userService,
        boardResponseFactory);
  }

  @Test
  void returns_a_board() {
    Board board = new Board(BOARD_ID, BOARD_TITLE, emptyList());
    when(userService.findByEmail("john.doe@codurance.com")).thenReturn(USER);
    when(boardRepository.getBoard(BOARD_ID)).thenReturn(board);
    when(boardResponseFactory.create(board, USER.getId()))
        .thenReturn(new BoardResponseObject(BOARD_ID, BOARD_TITLE, emptyList()));

    BoardResponseObject boardResponse = boardService.getBoard(USER, BOARD_ID);

    assertEquals(BOARD_ID, boardResponse.getId());
    assertEquals(BOARD_TITLE, boardResponse.getTitle());
  }

  @Test
  void creates_a_board() {
    NewBoardRequestObject requestObject = new NewBoardRequestObject(BOARD_TITLE,
        "john.doe@codurance.com");
    requestObject.setUser(USER);
    Board board = new Board(BOARD_ID, BOARD_TITLE, emptyList());
    when(boardFactory.create(requestObject)).thenReturn(board);
    when(boardRepository.insert(board)).thenReturn(board);

    Board boardResponse = boardService.createBoard(requestObject);

    assertEquals(BOARD_ID, boardResponse.getId());
    assertEquals(BOARD_TITLE, boardResponse.getTitle());
    assertEquals(0, boardResponse.getColumns().size());
  }

  @Test
  void should_return_boards_for_a_user() {
    when(userService.findOrCreateBy(USER))
        .thenReturn(new User(USER_ID, USER.email, USER.username));
    when(boardRepository.getUsersBoards(USER_ID)).thenReturn(List.of(
        new Board(BOARD_ID, BOARD_TITLE, emptyList())));

    List<Board> boards = boardService.getUsersBoards(USER);

    assertEquals(1, boards.size());
    assertEquals(BOARD_ID, boards.get(0).getId());
    assertEquals(BOARD_TITLE, boards.get(0).getTitle());
  }

  @Test
  public void should_throw_BoardNotFoundException_when_boardId_is_invalid_on_() {
    doThrow(new RuntimeException()).when(userService).registerUserIfNotExists(USER, BOARD_ID);
    assertThrows(BoardNotFoundException.class, () -> boardService.getBoard(USER, BOARD_ID));
  }

}
