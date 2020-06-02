package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.Board;

public interface BoardRepository {

  Board getBoard(Long id);

  Board insert(Board board);
}
