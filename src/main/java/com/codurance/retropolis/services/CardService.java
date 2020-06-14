package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.exceptions.CardNotFoundException;
import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.exceptions.UserAlreadyUpvotedException;
import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.repositories.CardRepository;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpdateCardRequestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

  private final CardFactory cardFactory;
  private final CardRepository cardRepository;

  @Autowired
  public CardService(CardFactory cardFactory, CardRepository cardRepository) {
    this.cardFactory = cardFactory;
    this.cardRepository = cardRepository;
  }

  public Card create(NewCardRequestObject requestObject) {
    Card card;
    Card newCard = cardFactory.create(requestObject);

    try {
      card = cardRepository.addCard(newCard);
    } catch (RuntimeException exception) {
      throw new ColumnNotFoundException();
    }
    return card;
  }

  public void delete(Long cardId) {
    try {
      cardRepository.delete(cardId);
    } catch (RuntimeException exception) {
      throw new CardNotFoundException();
    }
  }

  public Card updateText(Long cardId, UpdateCardRequestObject requestObject) {
    try {
      return cardRepository.updateText(cardId, requestObject.getNewText());
    } catch (RuntimeException invalidCardId) {
      throw new CardNotFoundException();
    }
  }

  public Card addUpvote(Long cardId, Long userId) {
    try {
      Card updatedCard = cardRepository.addUpvote(cardId, userId);
      return updatedCard;
    } catch (UserAlreadyUpvotedException userUpvotedException) {
      throw userUpvotedException;
    } catch (RuntimeException invalidCardId) {
      throw new CardNotFoundException();
    }
  }

  public Card removeUpvote(Long cardId, Long userId) {
    try {
      Card updatedCard = cardRepository.removeUpvote(cardId, userId);
      return updatedCard;
    } catch (RuntimeException invalidCardId) {
      throw new CardNotFoundException();
    }
  }
}
