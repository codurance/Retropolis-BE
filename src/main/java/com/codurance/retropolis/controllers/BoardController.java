package com.codurance.retropolis.controllers;


import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.services.BoardService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
public class BoardController extends BaseController {

  private final BoardService boardService;

  @Autowired
  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  @GetMapping(value = "")
  public List<Board> getUsersBoards() {
    long userId = 1L;
    return boardService.getUsersBoards(userId);
  }

  @GetMapping(value = "/{id}")
  public Board getBoard(@PathVariable long id) {
    return boardService.getBoard(id);
  }
}