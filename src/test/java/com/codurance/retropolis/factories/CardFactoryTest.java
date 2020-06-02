package com.codurance.retropolis.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import org.junit.jupiter.api.Test;

public class CardFactoryTest {

  @Test
  void returns_a_new_card() {
    String text = "hello";
    Long columnId = 1L;
    String username = "John Doe";
    NewCardRequestObject newCardRequestObject = new NewCardRequestObject(text, columnId, username);

    Card card = new CardFactory().create(newCardRequestObject);

    assertEquals(text, card.getText());
    assertEquals(columnId, card.getColumnId());
    assertEquals(username, card.getUsername());
  }

}
