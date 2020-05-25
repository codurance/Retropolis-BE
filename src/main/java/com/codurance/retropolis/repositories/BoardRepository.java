package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;

public interface BoardRepository {

  Board getBoard(Integer id);

  void addCard(Card card);
}
