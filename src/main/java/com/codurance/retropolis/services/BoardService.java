package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.User;
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
  private final UserBoardService userBoardService;

  @Autowired
  public BoardService(BoardRepository boardRepository, BoardFactory boardFactory,
      UserService userService, UserBoardService userBoardService) {
    this.boardRepository = boardRepository;
    this.boardFactory = boardFactory;
    this.userService = userService;
    this.userBoardService = userBoardService;
  }

  public Board getBoard(Long id) {
    return boardRepository.getBoard(id);
  }

  public Board createBoard(NewBoardRequestObject requestObject) {
    Board board = boardFactory.create(requestObject);
    Board savedBoard = boardRepository.insert(board);
    User user = userService.findOrCreateBy(requestObject.getUserEmail());
    userBoardService.addToBoard(user.id, savedBoard.getId());
    return savedBoard;
  }
}
