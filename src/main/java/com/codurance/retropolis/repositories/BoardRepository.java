package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Board;
import java.util.List;

public interface BoardRepository {

  Board getBoard(Long id);

  List<Board> getUsersBoards(long userId);
}
