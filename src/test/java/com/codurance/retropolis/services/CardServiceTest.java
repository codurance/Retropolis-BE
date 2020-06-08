package com.codurance.retropolis.services;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.User;
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

  private final Long NON_EXISTENT_CARD_ID = 999L;
  private final Long CARD_ID = 1L;
  private final Long COLUMN_ID = 1L;
  private final String USERNAME = "John Doe";
  private final String USER_EMAIL = "john.doe@codurance.com";
  private final Long USER_ID = 1L;
  private final User USER = new User(USER_ID, USER_EMAIL, USERNAME);
  private final String UPVOTING_USERNAME = "John Doe";
  private final String TEXT = "Hello";
  private final String NEW_TEXT = "updated hello";

  @Mock
  private CardFactory cardFactory;

  @Mock
  private CardRepository cardRepository;

  @Mock
  private UserService userService;

  private CardService cardService;

  @BeforeEach
  void setUp() {
    cardService = new CardService(cardFactory, cardRepository, userService);
  }

  @Test
  public void should_add_and_return_new_card() {
    when(userService.findByEmail(USER_EMAIL)).thenReturn(new User(USER_ID, USER_EMAIL, USERNAME));
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, USER_EMAIL);
    Card card = new Card(CARD_ID, TEXT, COLUMN_ID, USER_ID, emptyList());

    when(cardFactory.create(requestObject)).thenReturn(card);

    cardService.addCard(requestObject);

    verify(cardFactory).create(requestObject);
    verify(cardRepository).addCard(card);
  }

  @Test
  public void should_delete_card_from_the_repository() {
    cardService.delete(CARD_ID);
    verify(cardRepository).delete(CARD_ID);
  }

  @Test
  public void should_throw_CardNotFoundException_when_cardId_is_invalid_on_delete() {
    doThrow(new RuntimeException()).when(cardRepository).delete(NON_EXISTENT_CARD_ID);
    assertThrows(CardNotFoundException.class, () -> cardService.delete(NON_EXISTENT_CARD_ID));
  }

  @Test
  void should_change_card_text_and_return_edited_card() {
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject(NEW_TEXT);
    Card editedCard = new Card(CARD_ID, NEW_TEXT, COLUMN_ID, USER_ID, emptyList());
    when(cardRepository.updateText(CARD_ID, requestObject.getNewText())).thenReturn(editedCard);

    Card card = cardService.update(CARD_ID, requestObject);

    assertEquals(NEW_TEXT, card.getText());
  }

  @Test
  void should_add_card_voter_and_return_card() {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(USER_EMAIL);
    Card editedCard = new Card(CARD_ID, TEXT, COLUMN_ID, USER_ID, Collections.singletonList(USER.getId()));
    when(userService.findByEmail(USER_EMAIL)).thenReturn(USER);
    when(cardRepository.addVoter(CARD_ID, USER.getId())).thenReturn(editedCard);

    Card card = cardService.updateVotes(CARD_ID, requestObject);

    assertEquals(1, card.getVoters().size());
    assertEquals(USER.getId(), card.getVoters().get(0));
  }

  @Test
  public void should_throw_UserUpvotedException_when_username_exists_on_update_votes() {
    when(userService.findByEmail(USER_EMAIL)).thenReturn(USER);
    doThrow(new UserUpvotedException()).when(cardRepository).addVoter(CARD_ID, USER.getId());
    assertThrows(UserUpvotedException.class, () -> {
      UpVoteRequestObject requestObject = new UpVoteRequestObject(USER_EMAIL);
      cardService.updateVotes(CARD_ID, requestObject);
    });
  }

  @Test
  public void should_throw_CardNotFoundException_on_update_votes() {
    when(userService.findByEmail(USER_EMAIL)).thenReturn(USER);
    doThrow(new RuntimeException()).when(cardRepository).addVoter(NON_EXISTENT_CARD_ID, USER.getId());
    assertThrows(CardNotFoundException.class, () -> {
      UpVoteRequestObject requestObject = new UpVoteRequestObject(USER_EMAIL);
      cardService.updateVotes(NON_EXISTENT_CARD_ID, requestObject);
    });
  }

  @Test
  public void should_throw_CardNotFoundException_on_edit_card_text() {
    doThrow(new RuntimeException()).when(cardRepository).updateText(NON_EXISTENT_CARD_ID, NEW_TEXT);
    assertThrows(CardNotFoundException.class,
        () -> cardService.update(NON_EXISTENT_CARD_ID, new UpdateCardRequestObject(NEW_TEXT)));
  }

  @Test
  public void should_throw_ColumnNotFoundException_on_add_card() {
    when(userService.findByEmail(USER_EMAIL)).thenReturn(new User(USER_ID, USER_EMAIL, USERNAME));
    doThrow(new RuntimeException()).when(cardRepository).addCard(new Card());
    assertThrows(ColumnNotFoundException.class, () -> cardService.addCard(new NewCardRequestObject(TEXT, COLUMN_ID, USER_EMAIL)));
  }

}
