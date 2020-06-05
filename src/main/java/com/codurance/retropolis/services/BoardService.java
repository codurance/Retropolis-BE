package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.factories.BoardFactory;
import com.codurance.retropolis.repositories.BoardRepository;
import com.codurance.retropolis.requests.NewBoardRequestObject;
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

  public Board getBoard(String email, Long boardId) {
    try {
      userService.registerUserIfNotExists(email, boardId);
      return boardRepository.getBoard(boardId);
    } catch (RuntimeException exc) {
      throw new BoardNotFoundException();
    }
  }

  public Board createBoard(NewBoardRequestObject requestObject) {
    Board savedBoard = boardRepository.insert(boardFactory.create(requestObject));
    userService.registerUserIfNotExists(requestObject.getUserEmail(), savedBoard.getId());
    return savedBoard;
  }

  public List<Board> getUsersBoards(String email) {
    User user = userService.findOrCreateBy(email);
    return boardRepository.getUsersBoards(user.getId());
  }
}
