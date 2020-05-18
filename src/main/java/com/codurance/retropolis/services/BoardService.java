package com.codurance.retropolis.services;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.repositories.InMemoryBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {


  private InMemoryBoardRepository boardRepository;

  @Autowired
  public BoardService(InMemoryBoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public Board getBoard() {
    return boardRepository.getBoard();
  }
}
