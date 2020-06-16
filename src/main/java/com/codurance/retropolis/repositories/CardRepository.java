package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.Card;

public interface CardRepository {

  Card addCard(Card newCard);

  void delete(Long cardId);

  Card updateText(Long cardId, String newText);

  void addUpvote(Long cardId, Long userId);

  void removeUpvote(Long cardId, Long userId);
}
