package com.codurance.retropolis.responses;

import com.codurance.retropolis.entities.Card;
import org.springframework.stereotype.Component;

@Component
public class CardResponseObjectFactory {

  public CardResponseObject create(Card newCard, Long userId, String author) {
    Integer numberOfVoters = newCard.getVoters().size();
    boolean hasVoted = newCard.getVoters().contains(userId);
    return new CardResponseObject(newCard.getText(), newCard.getId(), newCard.getColumnId(),
        hasVoted, numberOfVoters, author);
  }

  public CardUpdatedTextResponseObject create(Card card) {
    return new CardUpdatedTextResponseObject(card.getId(), card.getText(), card.getColumnId());
  }
}
