package com.codurance.retropolis.controllers;

import com.codurance.retropolis.exceptions.CardNotFoundException;
import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpdateCardRequestObject;
import com.codurance.retropolis.services.CardService;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cards")
public class CardController extends BaseController {

  private final CardService cardService;

  @Autowired
  public CardController(CardService cardService) {
    this.cardService = cardService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Card postCard(@RequestBody @Valid NewCardRequestObject request) {
    return cardService.addCard(request);
  }

  @DeleteMapping(value = "/{cardId}")
  public ResponseEntity<HttpStatus> deleteCard(@PathVariable Long cardId) {
    cardService.delete(cardId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // todo add validation
  @PatchMapping(value = "/{cardId}")
  public Card updateCard(@PathVariable Long cardId, @RequestBody UpdateCardRequestObject request) {
    return cardService.update(cardId, request);
  }


  @ExceptionHandler(ColumnNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<String> handleColumnNotFound(ColumnNotFoundException exception) {
    return Collections.singletonList(exception.getMessage());
  }

  @ExceptionHandler(CardNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<String> handleCardNotFound(CardNotFoundException exception) {
    return Collections.singletonList(exception.getMessage());
  }

}