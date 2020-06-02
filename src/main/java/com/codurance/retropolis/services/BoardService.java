package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.repositories.BoardRepository;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

  private final BoardRepository boardRepository;

  @Autowired
  public BoardService(BoardRepository boardRepository) {
    this.boardRepository = boardRepository;
  }

  public Board getBoard(Long id) {
    return boardRepository.getBoard(id);
  }

  public void addToBoard(Long userId, Long boardId) {
    boardRepository.addToBoard(userId, boardId);
  }

  public Board createBoard(NewBoardRequestObject requestObject) {
    throw new UnsupportedOperationException("Implement me!");
  }
}
