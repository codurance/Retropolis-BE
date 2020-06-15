package com.codurance.retropolis.applicationservices;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.services.CardService;
import com.codurance.retropolis.services.UserService;
import com.codurance.retropolis.web.requests.NewCardRequestObject;
import com.codurance.retropolis.web.requests.UpVoteRequestObject;
import com.codurance.retropolis.web.responses.CardResponseObject;
import com.codurance.retropolis.web.responses.CardResponseObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ApplicationCardServiceTest {

  private final Boolean HAVE_VOTED = false;
  private final String EMAIL = "john.doe@codurance.com";
  private final String USERNAME = "John Doe";
  private final User USER = new User(3L, "john.doe@codurance.com", "John Doe");
  private final Long CARD_ID = 1L;
  private final Long COLUMN_ID = 2L;
  private final String TEXT = "Hello";
  @Mock
  UserService userService;
  @Mock
  CardService cardService;
  @Mock
  CardResponseObjectFactory cardResponseObjectFactory;
  private ApplicationCardService applicationCardService;
  private NewCardRequestObject newCardRequestObject;
  private UpVoteRequestObject upvoteRequestObject;


  @BeforeEach
  void setUp() {
    applicationCardService = new ApplicationCardService(userService, cardService,
        cardResponseObjectFactory);

    newCardRequestObject = new NewCardRequestObject(TEXT, COLUMN_ID, USER.email);
  }

  @Test
  void creates_card_response_object() {
    Card card = new Card(CARD_ID, TEXT, COLUMN_ID, USER.getId(), emptyList());

    User author = new User(USER.getId(), USER.email, USER.username);
    CardResponseObject cardResponseObject = new CardResponseObject(card.getText(), card.getId(),
        card.getColumnId(), HAVE_VOTED, card.getVoters().size(), author.username);

    when(userService.findByEmail(newCardRequestObject.getEmail())).thenReturn(author);
    when(cardService.create(newCardRequestObject)).thenReturn(card);

    when(userService.findById(author.getId())).thenReturn(author);
    when(cardResponseObjectFactory.create(card, author.getId(), author.username))
        .thenReturn(cardResponseObject);

    CardResponseObject response = applicationCardService.create(newCardRequestObject);

    assertEquals(CARD_ID, response.getId());
    assertEquals(HAVE_VOTED, response.getHaveVoted());
    assertEquals(card.getVoters().size(), response.getTotalVoters());
    assertEquals(TEXT, response.getText());
    assertEquals(card.getColumnId(), response.getColumnId());
  }

  @Test
  void adds_upvote_to_card_response() {
    upvoteRequestObject = new UpVoteRequestObject(EMAIL, true);
    ResponseEntity<HttpStatus> response = applicationCardService.addUpvote(CARD_ID, upvoteRequestObject);
    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  void removes_upvote_from_card_response() {
    upvoteRequestObject = new UpVoteRequestObject(EMAIL, false);
    ResponseEntity<HttpStatus> response = applicationCardService.removeUpvote(CARD_ID, upvoteRequestObject);
    assertEquals(200, response.getStatusCode().value());
  }
}
