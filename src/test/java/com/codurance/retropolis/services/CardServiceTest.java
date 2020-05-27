package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.exceptions.CardNotFoundException;
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

  public static final long NON_EXISTENT_CARD_ID = 999L;

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

  @Test
  public void should_delete_card_from_the_repository() {
    Long cardId = 1L;
    cardService.delete(cardId);
    verify(cardRepository).delete(cardId);
  }

  @Test
  public void should_throw_CardNotFoundException_when_cardId_is_invalid() {
    doThrow(new RuntimeException()).when(cardRepository).delete(NON_EXISTENT_CARD_ID);
    assertThrows(CardNotFoundException.class, () -> cardService.delete(NON_EXISTENT_CARD_ID));
  }

}
