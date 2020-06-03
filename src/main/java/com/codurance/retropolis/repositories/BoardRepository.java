package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.Board;
import java.util.List;

public interface BoardRepository {

  Board getBoard(Long id);

  List<Board> getUsersBoards(Long userId);

  Board insert(Board board);
}
