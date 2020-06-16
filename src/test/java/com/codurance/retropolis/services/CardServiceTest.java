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
import com.codurance.retropolis.exceptions.UserAlreadyUpvotedException;
import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.repositories.CardRepository;
import com.codurance.retropolis.web.requests.NewCardRequestObject;
import com.codurance.retropolis.web.requests.UpVoteRequestObject;
import com.codurance.retropolis.web.requests.UpdateCardRequestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

  private final Long NON_EXISTENT_CARD_ID = 999L;
  private final Long CARD_ID = 1L;
  private final Long COLUMN_ID = 2L;
  private final User USER = new User(3L, "john.doe@codurance.com", "John Doe");
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
    cardService = new CardService(userService, cardFactory, cardRepository);
  }

  @Test
  public void should_create_and_return_new_card() {
    Card card = new Card(CARD_ID, TEXT, COLUMN_ID, USER.getId(), emptyList());
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, USER.email);
    when(cardFactory.create(requestObject)).thenReturn(card);
    when(cardRepository.addCard(card)).thenReturn(card);
    cardService.create(requestObject);

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
    Card editedCard = new Card(CARD_ID, NEW_TEXT, COLUMN_ID, USER.getId(), emptyList());
    when(cardRepository.updateText(editedCard.getId(), requestObject.getNewText())).thenReturn(editedCard);

    Card card = cardService.updateText(editedCard.getId(), requestObject);

    assertEquals(NEW_TEXT, card.getText());
  }

  @Test
  void should_add_card_voter_and_return_card() {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(USER.email, true);
    when(userService.findByEmail(requestObject.getEmail())).thenReturn(USER);
    cardService.addUpvote(CARD_ID, requestObject);

    verify(cardRepository).addUpvote(CARD_ID, USER.getId());
  }

  @Test
  public void should_throw_UserAlreadyUpvotedException_when_user_has_already_upvoted() {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(USER.email, true);
    when(userService.findByEmail(requestObject.getEmail())).thenReturn(USER);

    doThrow(new UserAlreadyUpvotedException()).when(cardRepository)
        .addUpvote(CARD_ID, USER.getId());

    assertThrows(UserAlreadyUpvotedException.class,
        () -> cardService.addUpvote(CARD_ID, requestObject));
  }

  @Test
  public void should_throw_CardNotFoundException_on_upvote() {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(USER.email, true);
    when(userService.findByEmail(requestObject.getEmail())).thenReturn(USER);

    doThrow(new RuntimeException()).when(cardRepository)
        .addUpvote(NON_EXISTENT_CARD_ID, USER.getId());
    assertThrows(CardNotFoundException.class,
        () -> cardService.addUpvote(NON_EXISTENT_CARD_ID, requestObject));
  }

  @Test
  void should_remove_card_voter_and_return_card() {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(USER.email, false);
    when(userService.findByEmail(requestObject.getEmail())).thenReturn(USER);

    cardService.removeUpvote(CARD_ID, requestObject);

    verify(cardRepository).removeUpvote(CARD_ID, USER.getId());
  }

  @Test
  public void should_throw_CardNotFoundException_on_updateText() {
    doThrow(new RuntimeException()).when(cardRepository).updateText(NON_EXISTENT_CARD_ID, NEW_TEXT);
    assertThrows(CardNotFoundException.class,
        () -> cardService.updateText(NON_EXISTENT_CARD_ID, new UpdateCardRequestObject(NEW_TEXT)));
  }

  @Test
  public void should_throw_ColumnNotFoundException_on_create() {
    when(userService.findByEmail(USER.email)).thenReturn(new User(USER.getId(), USER.email, USER.username));
    doThrow(new RuntimeException()).when(cardRepository).addCard(new Card());
    assertThrows(ColumnNotFoundException.class, () -> cardService.create(new NewCardRequestObject(TEXT, COLUMN_ID, USER.email)));
  }

}
