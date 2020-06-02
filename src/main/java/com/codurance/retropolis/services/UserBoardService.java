package com.codurance.retropolis.services;

import com.codurance.retropolis.repositories.BoardRepository;
import org.springframework.stereotype.Service;

@Service
public class UserBoardService {

  private final BoardRepository boardRepository;

  public UserBoardService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public void addToBoard(Long userId, Long boardId) {
    boardRepository.addToBoard(userId, boardId);
  }

}
