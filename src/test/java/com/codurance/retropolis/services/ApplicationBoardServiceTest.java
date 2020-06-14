package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.responses.BoardResponseObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ApplicationBoardServiceTest {

  private final User USER = new User("john.doe@codurance.com", "John Doe");
  private final Long BOARD_ID = 1L;
  ApplicationBoardService applicationBoardService;
  @Mock
  private UserService userService;
  @Mock
  private BoardService boardService;

  @Mock
  private BoardResponseObjectFactory boardResponseFactory;


  @BeforeEach
  void setUp() {
    applicationBoardService = new ApplicationBoardService(userService, boardResponseFactory,
        boardService);
  }

  @Test
  public void should_throw_BoardNotFoundException_when_boardId_is_invalid_on_() {
    doThrow(new RuntimeException()).when(userService).registerUserIfNotExists(USER, BOARD_ID);
    assertThrows(BoardNotFoundException.class,
        () -> applicationBoardService.getBoard(USER, BOARD_ID));
  }
}
