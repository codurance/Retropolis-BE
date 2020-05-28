package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Card;

public interface CardRepository {

  Card insert(Card newCard);

  void delete(Long cardId);
}
