package com.codurance.retropolis.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import org.junit.jupiter.api.Test;

public class CardFactoryTest {

  @Test
  void returns_a_new_card() {
    NewCardRequestObject newCardRequestObject = new NewCardRequestObject("hello", 1, "John Doe");
    CardFactory cardFactory = new CardFactory();
    Card card = cardFactory.create(newCardRequestObject);
    assertEquals("hello", card.getText());
    assertEquals(1, card.getColumnId());
    assertEquals("John Doe", card.getUserName());
  }

}
