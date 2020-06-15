package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.factories.BoardFactory;
import com.codurance.retropolis.repositories.BoardRepository;
import com.codurance.retropolis.web.requests.NewBoardRequestObject;
import java.util.List;
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

  public Board getBoard(User user, Long boardId) {
    try {
      userService.registerUserIfNotExists(user, boardId);
      return boardRepository.getBoard(boardId);
    } catch (RuntimeException exc) {
      throw new BoardNotFoundException();
    }
  }

  public Board createBoard(NewBoardRequestObject requestObject) {
    Board savedBoard = boardRepository.insert(boardFactory.create(requestObject));
    userService.registerUserIfNotExists(requestObject.getUser(), savedBoard.getId());
    return savedBoard;
  }

  public List<Board> getUsersBoards(User requestUser) {
    User user = userService.findOrCreateBy(requestUser);
    return boardRepository.getUsersBoards(user.getId());
  }
}
