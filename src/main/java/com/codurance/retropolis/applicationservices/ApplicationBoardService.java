package com.codurance.retropolis.applicationservices;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.services.BoardService;
import com.codurance.retropolis.services.UserService;
import com.codurance.retropolis.web.requests.NewBoardRequestObject;
import com.codurance.retropolis.web.responses.BoardResponseObject;
import com.codurance.retropolis.web.responses.BoardResponseObjectFactory;
import com.codurance.retropolis.web.responses.UserBoardResponseObject;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ApplicationBoardService {

  private final UserService userService;
  private final BoardResponseObjectFactory boardResponseObjectFactory;
  private final BoardService boardService;

  public ApplicationBoardService(UserService userService,
      BoardResponseObjectFactory boardResponseObjectFactory, BoardService boardService) {
    this.userService = userService;
    this.boardResponseObjectFactory = boardResponseObjectFactory;
    this.boardService = boardService;
  }

  public BoardResponseObject getBoard(User requestUser, Long boardId) {
    try {
      Board board = boardService.getBoard(requestUser, boardId);
      return getResponseFrom(board, requestUser);
    } catch (RuntimeException exc) {
      throw new BoardNotFoundException();
    }
  }

  public List<UserBoardResponseObject> getUserBoards(User requestUser) {
    List<Board> boards = boardService.getUsersBoards(requestUser);
    return getResponseFrom(boards);
  }

  public BoardResponseObject createBoard(NewBoardRequestObject requestObject) {
    Board board = boardService.createBoard(requestObject);
    return boardResponseObjectFactory.create(board, requestObject.getUser().getId());
  }

  private BoardResponseObject getResponseFrom(Board board, User requestUser) {
    User user = userService.findByEmail(requestUser.email);
    return boardResponseObjectFactory.create(board, user.getId());
  }

  private List<UserBoardResponseObject> getResponseFrom(List<Board> boards) {
    return boardResponseObjectFactory.create(boards);
  }

}
