package com.codurance.retropolis.services;

import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;

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

  private final Long BOARD_ID = 1L;
  private final Long USER_ID = 1L;
  private final String USERNAME = "John Doe";
  private final String USER_EMAIL = "john.doe@codurance.com";
  private final User USER = new User(USER_EMAIL, USERNAME);

  @Mock
  private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository);
  }

  @Test
  void calls_addToBoard_after_registration() {
    when(userRepository.findByEmail(USER_EMAIL)).thenReturn(new User(USER_ID, USER_EMAIL, USERNAME));

    userService.registerUserIfNotExists(USER, BOARD_ID);
    verify(userRepository).addToBoard(USER_ID, BOARD_ID);
  }

  @Test
  void adds_user_to_board() {
    userService.addToBoard(USER_ID, BOARD_ID);
    verify(userRepository).addToBoard(USER_ID, BOARD_ID);
  }

  @Test
  void findByEmail_returns_user_from_repo() {
    userService.findByEmail(USER_EMAIL);
    verify(userRepository).findByEmail(USER_EMAIL);
  }

  @Test
  void findById_returns_user_from_repo() {
    userService.findById(USER_ID);
    verify(userRepository).findById(USER_ID);
  }

  @Test
  void calls_userRepository_RegisterByEmail_and_registers_user_to_board_when_UserNotFoundException() {
    when(userRepository.findByEmail(USER_EMAIL)).thenThrow(new UserNotFoundException());
    when(userRepository.register(USER)).thenReturn(new User(USER_ID, USER_EMAIL, USERNAME));

    userService.registerUserIfNotExists(USER, BOARD_ID);

    verify(userRepository).register(USER);
    verify(userRepository).addToBoard(USER_ID, BOARD_ID);
  }

  @Test
  void registers_user_when_UserNotFoundException() {
    when(userRepository.findByEmail(USER_EMAIL)).thenThrow(new UserNotFoundException());
    userService.findOrCreateBy(USER);
    verify(userRepository).register(USER);
  }

}
