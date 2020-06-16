package com.codurance.retropolis.factories;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.web.requests.NewCardRequestObject;
import java.util.Collections;
import org.springframework.stereotype.Component;

@Component
public class CardFactory {

  public Card create(NewCardRequestObject requestObject) {
    return new Card(requestObject.getText(), requestObject.getColumnId(), requestObject.getUserId(),
        Collections.emptyList());
  }

}