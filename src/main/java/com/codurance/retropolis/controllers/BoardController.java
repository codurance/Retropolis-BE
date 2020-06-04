package com.codurance.retropolis.controllers;


import com.codurance.retropolis.config.web.GoogleTokenAuthenticator;
import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import com.codurance.retropolis.services.BoardService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
public class BoardController extends BaseController {

  private final BoardService boardService;
  private final GoogleTokenAuthenticator tokenAuthenticator;

  @Autowired
  public BoardController(BoardService boardService, GoogleTokenAuthenticator tokenAuthenticator) {
    this.boardService = boardService;
    this.tokenAuthenticator = tokenAuthenticator;
  }

  @GetMapping
  public List<Board> getUsersBoards(@RequestHeader(HttpHeaders.AUTHORIZATION) String token)
      throws GeneralSecurityException, IOException {
    return boardService.getUsersBoards(tokenAuthenticator.getEmail(token));
  }

  @GetMapping(value = "/{id}")
  public Board getBoard(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token)
      throws GeneralSecurityException, IOException {
    return boardService.getBoard(tokenAuthenticator.getEmail(token), id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Board postBoard(@RequestBody @Valid NewBoardRequestObject request) {
    return boardService.createBoard(request);
  }

  @ExceptionHandler(BoardNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<String> handleBoardNotFound(BoardNotFoundException exception) {
    return Collections.singletonList(exception.getMessage());
  }
}