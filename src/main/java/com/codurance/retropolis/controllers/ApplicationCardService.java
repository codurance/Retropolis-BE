package com.codurance.retropolis.controllers;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.responses.CardResponseObject;
import com.codurance.retropolis.responses.CardResponseObjectFactory;
import com.codurance.retropolis.services.CardService;
import com.codurance.retropolis.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationCardService {

  private final UserService userService;
  private final CardService cardService;
  private CardResponseObjectFactory cardResponseObjectFactory;

  public ApplicationCardService(UserService userService, CardService cardService,
      CardResponseObjectFactory cardResponseObjectFactory) {
    this.userService = userService;
    this.cardService = cardService;
    this.cardResponseObjectFactory = cardResponseObjectFactory;
  }

  public CardResponseObject create(NewCardRequestObject requestObject) {
    User user = userService.findByEmail(requestObject.getEmail());
    requestObject.setUserId(user.getId());

    try {
      Card card = cardService.create(requestObject);
      return createResponseFrom(card, user.getId());
    } catch (RuntimeException exception) {
      throw new ColumnNotFoundException();
    }
  }

  private CardResponseObject createResponseFrom(Card card, Long userId) {
    User cardAuthor = userService.findById(userId);
    return cardResponseObjectFactory.create(card, userId, cardAuthor.username);
  }
}
