package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.factories.BoardFactory;
import com.codurance.retropolis.repositories.BoardRepository;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import com.codurance.retropolis.responses.BoardResponseObject;
import com.codurance.retropolis.responses.BoardResponseObjectFactory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

  private final BoardRepository boardRepository;
  private final BoardFactory boardFactory;
  private final UserService userService;
  private final BoardResponseObjectFactory boardResponseObjectFactory;

  @Autowired
  public BoardService(BoardRepository boardRepository, BoardFactory boardFactory,
      UserService userService, BoardResponseObjectFactory boardResponseObjectFactory) {
    this.boardRepository = boardRepository;
    this.boardFactory = boardFactory;
    this.userService = userService;
    this.boardResponseObjectFactory = boardResponseObjectFactory;
  }

  public BoardResponseObject getBoard(User user, Long boardId) {
    try {
      userService.registerUserIfNotExists(user, boardId);
      user = userService.findByEmail(user.email);
      Board board = boardRepository.getBoard(boardId);
      return getResponseFrom(board, user.getId());
    } catch (RuntimeException exc) {
      throw new BoardNotFoundException();
    }
  }

  private BoardResponseObject getResponseFrom(Board board, Long userId) {
    return boardResponseObjectFactory.create(board, userId);
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
