package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Card;

public interface CardRepository {

  Card addCard(Card newCard);

  void delete(Long cardId);

    Card update(Long cardId, String newText);

  Card addVoter(Long cardId, String username);
}
