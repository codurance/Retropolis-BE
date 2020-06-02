package com.codurance.retropolis.controllers;


import com.codurance.retropolis.config.GoogleTokenAuthenticator;
import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import com.codurance.retropolis.services.BoardService;
import com.codurance.retropolis.services.UserService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  private final UserService userService;

  @Autowired
  public BoardController(BoardService boardService, GoogleTokenAuthenticator tokenAuthenticator, UserService userService) {
    this.boardService = boardService;
    this.tokenAuthenticator = tokenAuthenticator;
    this.userService = userService;
  }

  @GetMapping(value = "/{id}")
  public Board getBoard(@PathVariable Long id, @RequestHeader(HttpHeaders.AUTHORIZATION) String token)
      throws GeneralSecurityException, IOException {
    userService.registerUserIfNotExists(tokenAuthenticator.getEmail(token), id);
    return boardService.getBoard(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Board postBoard(@RequestBody NewBoardRequestObject request) {
    return boardService.createBoard(request);
  }
}