package com.codurance.retropolis.services;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.UserNotFoundException;
import com.codurance.retropolis.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  public static final Long BOARD_ID = 1L;
  public static final Long USER_ID = 1L;
  public static final String EMAIL = "john.doe@codurance.com";

  @Mock
  private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository);
  }

  @Test
  void calls_addToBoard_after_registration() {
    when(userRepository.findByEmail(EMAIL)).thenReturn(new User(USER_ID, EMAIL));

    userService.registerUserIfNotExists(EMAIL, BOARD_ID);
    verify(userRepository).addToBoard(USER_ID, BOARD_ID);
  }

  @Test
  void adds_user_to_board() {
    userService.addToBoard(USER_ID, BOARD_ID);
    verify(userRepository).addToBoard(USER_ID, BOARD_ID);
  }

  @Test
  void calls_userRepository_RegisterByEmail_when_UserNotFoundException() {
    given(userRepository.findByEmail(EMAIL)).willThrow(new UserNotFoundException());
    given(userRepository.register(EMAIL)).willReturn(new User(USER_ID, EMAIL));
    userService.registerUserIfNotExists(EMAIL, BOARD_ID);
    verify(userRepository).register(EMAIL);
    verify(userRepository).addToBoard(USER_ID, BOARD_ID);
  }
}
