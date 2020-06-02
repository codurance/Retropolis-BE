package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.repositories.BoardRepository;
import java.util.List;
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

  public List<Board> getUsersBoards(long userId) {
    return boardRepository.getUsersBoards(userId);
  }
}
