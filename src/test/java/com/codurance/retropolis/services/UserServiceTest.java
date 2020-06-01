package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.exceptions.UserNotFoundException;
import com.codurance.retropolis.models.User;
import com.codurance.retropolis.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  public static final long BOARD_ID = 1L;
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BoardService boardService;
  public static final String EMAIL = "john.doe@codurance.com";
  public static final Long USER_ID = BOARD_ID;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository, boardService);
  }

  @Test
  void calls_board_service_addToBoard() {
    when(userRepository.findByEmail(EMAIL)).thenReturn(new User(USER_ID, EMAIL));

    userService.registerUserIfNotExists(EMAIL, BOARD_ID);
    verify(boardService).addToBoard(USER_ID, BOARD_ID);
  }

  @Test
  void calls_userRepository_RegisterByEmail_when_UserNotFoundException() {
    given(userRepository.findByEmail(EMAIL)).willThrow(new UserNotFoundException());
    given(userRepository.registerBy(EMAIL)).willReturn(new User(USER_ID, EMAIL));
    userService.registerUserIfNotExists(EMAIL, BOARD_ID);
    verify(userRepository).registerBy(EMAIL);
    verify(boardService).addToBoard(USER_ID, BOARD_ID);
  }
}
