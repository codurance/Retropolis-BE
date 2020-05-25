package com.codurance.retropolis.factories;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import org.springframework.stereotype.Component;

@Component
public class CardFactory {

  public Card create(NewCardRequestObject requestObject) {
    int id = getCardId();
    return new Card(id, requestObject.getText(), requestObject.getColumnId(), requestObject.getUserName());
  }

  protected int getCardId() {
    return CardIDGenerator.nextID();
  }
}