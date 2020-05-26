package com.codurance.retropolis.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.repositories.CardRepository;
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

  @Mock
  private CardRepository cardRepository;

  private CardService cardService;

  @BeforeEach
  void setUp() {
    cardService = new CardService(cardFactory, cardRepository);
  }

  @Test
  public void should_add_and_return_new_card() {
    String text = "new card";
    Long columnId = 1L;
    Long cardId = 1L;
    String userName = "John Doe";
    NewCardRequestObject requestObject = new NewCardRequestObject(text, columnId, userName);

    Card card = new Card(cardId, text, columnId, userName);
    when(cardFactory.create(requestObject)).thenReturn(card);

    cardService.addCard(requestObject);

    verify(cardFactory).create(requestObject);
    verify(cardRepository).insert(card);
  }

}
