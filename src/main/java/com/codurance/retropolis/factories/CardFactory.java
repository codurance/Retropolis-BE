package com.codurance.retropolis.factories;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import org.springframework.stereotype.Component;

@Component
public class CardFactory {

  public Card create(NewCardRequestObject requestObject) {
    int id = CardIDGenerator.nextID();
    return new Card(id, requestObject.getText(), requestObject.getColumnId());
  }
}