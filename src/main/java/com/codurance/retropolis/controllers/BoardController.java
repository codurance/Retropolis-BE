package com.codurance.retropolis.controllers;


import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.services.BoardService;
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

  @GetMapping(value = "/{id}")
  public Board getBoard(@PathVariable Long id) {
    return boardService.getBoard(id);
  }
}