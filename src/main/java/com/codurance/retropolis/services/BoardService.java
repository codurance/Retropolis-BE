package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.factories.BoardFactory;
import com.codurance.retropolis.repositories.BoardRepository;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

  private final BoardRepository boardRepository;
  private final BoardFactory boardFactory;

  @Autowired
  public BoardService(BoardRepository boardRepository, BoardFactory boardFactory) {
    this.boardRepository = boardRepository;
    this.boardFactory = boardFactory;
  }

  public Board getBoard(Long id) {
    return boardRepository.getBoard(id);
  }

  public void addToBoard(Long userId, Long boardId) {
    boardRepository.addToBoard(userId, boardId);
  }

  public Board createBoard(NewBoardRequestObject requestObject) {
    Board board = boardFactory.create(requestObject);
    return boardRepository.insert(board);
  }


}
