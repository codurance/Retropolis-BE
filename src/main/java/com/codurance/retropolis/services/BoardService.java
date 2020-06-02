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
  private final UserService userService;

  @Autowired
  public BoardService(BoardRepository boardRepository, BoardFactory boardFactory,
      UserService userService) {
    this.boardRepository = boardRepository;
    this.boardFactory = boardFactory;
    this.userService = userService;
  }

  public Board getBoard(Long id) {
    return boardRepository.getBoard(id);
  }

  public Board createBoard(NewBoardRequestObject requestObject) {
    Board savedBoard = boardRepository.insert(boardFactory.create(requestObject));
    userService.registerUserIfNotExists(requestObject.getUserEmail(), savedBoard.getId());
    return savedBoard;
  }
}
