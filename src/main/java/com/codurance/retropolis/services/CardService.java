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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {

  private final CardFactory cardFactory;
  private final CardRepository cardRepository;
  private final UserService userService;

  @Autowired
  public CardService(UserService userService, CardFactory cardFactory, CardRepository cardRepository) {
    this.userService = userService;
    this.cardFactory = cardFactory;
    this.cardRepository = cardRepository;
  }

  public Card create(NewCardRequestObject requestObject) {
    try {
      return cardRepository.addCard(cardFactory.create(requestObject));
    } catch (RuntimeException exception) {
      throw new ColumnNotFoundException();
    }
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

  public void addUpvote(Long cardId, UpVoteRequestObject requestObject) {
    try {
      User user = userService.findByEmail(requestObject.getEmail());
      cardRepository.addUpvote(cardId, user.getId());
    } catch (UserAlreadyUpvotedException userUpvotedException) {
      throw userUpvotedException;
    } catch (RuntimeException invalidCardId) {
      throw new CardNotFoundException();
    }
  }

  public void removeUpvote(Long cardId, UpVoteRequestObject requestObject) {
    try {
      User user = userService.findByEmail(requestObject.getEmail());
      cardRepository.removeUpvote(cardId, user.getId());
    } catch (RuntimeException invalidCardId) {
      throw new CardNotFoundException();
    }
  }

}
