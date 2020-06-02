package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.Board;
import java.util.List;

public interface BoardRepository {

  Board getBoard(Long id);

  void addToBoard(Long userId, Long boardId);

  List<Board> getUsersBoards(Long userId);
}
