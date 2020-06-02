package com.codurance.retropolis.services;

import static org.mockito.Mockito.verify;

import com.codurance.retropolis.repositories.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserBoardServiceTest {

  public static final Long USER_ID = 1L;
  public static final Long BOARD_ID = 1L;

  @Mock
  private BoardRepository boardRepository;

  private UserBoardService userBoardService;

  @BeforeEach
  void setUp() {
    userBoardService = new UserBoardService(boardRepository);
  }


  @Test
  void adds_user_to_board() {
    userBoardService.addToBoard(USER_ID, BOARD_ID);
    verify(boardRepository).addToBoard(USER_ID, BOARD_ID);
  }

}