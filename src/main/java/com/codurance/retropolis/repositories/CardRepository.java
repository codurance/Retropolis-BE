package com.codurance.retropolis.repositories;

import com.codurance.retropolis.entities.Card;

public interface CardRepository {

  Card addCard(Card newCard);

  void delete(Long cardId);

  Card updateText(Long cardId, String newText);

  Card addUpvote(Long cardId, Long userId);

  Card removeUpvote(Long cardId, Long userId);
}
