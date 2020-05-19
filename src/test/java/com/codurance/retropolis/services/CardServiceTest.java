package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

  @Mock
  private CardFactory cardFactory;

  private CardService cardService;

  @Mock
  private BoardService boardService;

  @BeforeEach
  void setUp() {
    cardService = new CardService(cardFactory, boardService);
  }

  @Test
  public void should_add_and_return_new_card() {
    String text = "new card";
    int columnId = 1;
    int cardId = 1;
    NewCardRequestObject requestObject = new NewCardRequestObject(text, columnId);
    Card card = new Card(cardId, text, columnId);

    when(cardFactory.create(requestObject)).thenReturn(card);
    when(boardService.addCard(card)).thenReturn(card);

    assertEquals(card, cardService.addCard(requestObject));
  }

}
