package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.models.User;
import com.codurance.retropolis.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BoardService boardService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository, boardService);
  }

  @Test
  void calls_add_to_board_on_board_service() {
    String email = "john.doe@codurance.com";
    Long id = 1L;
    when(userRepository.findByEmail(email)).thenReturn(new User(id, email));

    userService.registerUserIfNotExists(email, 1L);
    verify(boardService).addToBoard(1L, 1L);
  }
}
