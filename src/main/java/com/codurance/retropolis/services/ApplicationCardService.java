package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import com.codurance.retropolis.requests.UpdateCardRequestObject;
import com.codurance.retropolis.responses.CardResponseObject;
import com.codurance.retropolis.responses.CardResponseObjectFactory;
import com.codurance.retropolis.responses.CardUpdatedTextResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    Card card = cardService.create(requestObject);
    return createResponseFrom(card, user.getId());
  }

  public ResponseEntity<HttpStatus> addUpvote(Long cardId, UpVoteRequestObject requestObject) {
    cardService.addUpvote(cardId, requestObject);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public ResponseEntity<HttpStatus> removeUpvote(Long cardId, UpVoteRequestObject requestObject) {
    cardService.removeUpvote(cardId, requestObject);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public ResponseEntity<HttpStatus> delete(Long cardId) {
    cardService.delete(cardId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public CardUpdatedTextResponseObject updateText(Long cardId, UpdateCardRequestObject request) {
    Card card = cardService.updateText(cardId, request);
    return createResponseFrom(card);
  }

  private CardResponseObject createResponseFrom(Card card, Long userId) {
    User cardAuthor = userService.findById(userId);
    return cardResponseObjectFactory.create(card, userId, cardAuthor.username);
  }

  private CardUpdatedTextResponseObject createResponseFrom(Card card) {
    return cardResponseObjectFactory.create(card);
  }
}
