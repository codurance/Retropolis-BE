package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.CardNotFoundException;
import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.exceptions.UserAlreadyUpvotedException;
import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.repositories.CardRepository;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import com.codurance.retropolis.requests.UpdateCardRequestObject;
import com.codurance.retropolis.responses.CardResponseObject;
import com.codurance.retropolis.responses.CardResponseObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

  private final CardFactory cardFactory;
  private final CardRepository cardRepository;
  private final UserService userService;
  private final CardResponseObjectFactory cardResponseObjectFactory;

  @Autowired
  public CardService(CardFactory cardFactory, CardRepository cardRepository,
      UserService userService, CardResponseObjectFactory cardResponseObjectFactory) {
    this.cardFactory = cardFactory;
    this.cardRepository = cardRepository;
    this.userService = userService;
    this.cardResponseObjectFactory = cardResponseObjectFactory;
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

  public CardResponseObject removeUpvote(Long cardId, UpVoteRequestObject requestObject) {
    try {
      User user = userService.findByEmail(requestObject.getEmail());
      Card updatedCard = cardRepository.removeUpvote(cardId, user.getId());
      return createResponseFrom(updatedCard, user.getId());
    } catch (RuntimeException invalidCardId) {
      throw new CardNotFoundException();
    }
  }

  private CardResponseObject createResponseFrom(Card card, Long userId) {
    User cardAuthor = userService.findById(userId);
    return cardResponseObjectFactory.create(card, userId, cardAuthor.username);
  }
}
