package com.codurance.retropolis.services;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

  private final BoardRepository boardRepository;

  @Autowired
  public BoardService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public Board getBoard() {
    return boardRepository.getBoard();
  }
}
