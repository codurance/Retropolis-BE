package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.exceptions.CardNotFoundException;
import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.exceptions.UserUpvotedException;
import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.repositories.CardRepository;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import com.codurance.retropolis.requests.UpdateCardRequestObject;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

  public static final Long NON_EXISTENT_CARD_ID = 999L;
  public static final Long CARD_ID = 1L;
  public static final String USERNAME = "John Doe";

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
    NewCardRequestObject requestObject = new NewCardRequestObject(text, columnId, USERNAME);

    Card card = new Card(cardId, text, columnId, USERNAME);
    when(cardFactory.create(requestObject)).thenReturn(card);

    cardService.addCard(requestObject);

    verify(cardFactory).create(requestObject);
    verify(cardRepository).addCard(card);
  }

  @Test
  public void should_delete_card_from_the_repository() {
    Long cardId = 1L;
    cardService.delete(cardId);
    verify(cardRepository).delete(cardId);
  }

  @Test
  public void should_throw_CardNotFoundException_when_cardId_is_invalid_on_delete() {
    doThrow(new RuntimeException()).when(cardRepository).delete(NON_EXISTENT_CARD_ID);
    assertThrows(CardNotFoundException.class, () -> cardService.delete(NON_EXISTENT_CARD_ID));
  }

  @Test
  void should_change_card_text_and_return_edited_card() {
    Long cardId = 1L;
    String newText = "updated hello";
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject(newText);
    Card editedCard = new Card(cardId, newText, 1L, USERNAME);
    when(cardRepository.updateText(cardId, requestObject.getNewText())).thenReturn(editedCard);

    Card card = cardService.update(cardId, requestObject);

    assertEquals(newText, card.getText());
  }

  @Test
  void should_add_card_voter_and_return_card() {
    Long cardId = 1L;
    String voter = "Jane Doe";
    String text = "Hello";
    UpVoteRequestObject requestObject = new UpVoteRequestObject(voter, true);
    Card editedCard = new Card(cardId, text, 1L, USERNAME, Collections.singletonList(voter));
    when(cardRepository.addVoter(cardId, requestObject.getUsername())).thenReturn(editedCard);

    Card card = cardService.updateVotes(cardId, requestObject);

    assertEquals(1, card.getVoters().size());
    assertEquals(voter, card.getVoters().get(0));
  }

  @Test
  public void should_throw_UserUpvotedException_when_username_exists_on_update_votes() {
    doThrow(new UserUpvotedException()).when(cardRepository).addVoter(CARD_ID, USERNAME);
    assertThrows(UserUpvotedException.class, () -> {
      UpVoteRequestObject requestObject = new UpVoteRequestObject(USERNAME, true);
      cardService.updateVotes(CARD_ID, requestObject);
    });
  }

  @Test
  public void should_throw_CardNotFoundException_on_update_votes() {
    doThrow(new RuntimeException()).when(cardRepository).addVoter(NON_EXISTENT_CARD_ID, USERNAME);
    assertThrows(CardNotFoundException.class, () -> {
      UpVoteRequestObject requestObject = new UpVoteRequestObject(USERNAME, true);
      cardService.updateVotes(NON_EXISTENT_CARD_ID, requestObject);
    });
  }

  @Test
  public void should_throw_CardNotFoundException_on_edit_card_text() {
    String updatedText = "updated text";
    doThrow(new RuntimeException()).when(cardRepository).updateText(NON_EXISTENT_CARD_ID, updatedText);
    assertThrows(CardNotFoundException.class,
        () -> cardService.update(NON_EXISTENT_CARD_ID, new UpdateCardRequestObject(updatedText)));
  }

  @Test
  public void should_throw_ColumnNotFoundException_on_add_card() {
    doThrow(new RuntimeException()).when(cardRepository).addCard(new Card());
    assertThrows(ColumnNotFoundException.class, () -> cardService.addCard(new NewCardRequestObject("text", 1L, USERNAME)));
  }


}
