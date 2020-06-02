package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.Board;

public interface BoardRepository {

  Board getBoard(Long id);

  void addToBoard(Long userId, Long boardId);

  Board insert(Board board);
}
