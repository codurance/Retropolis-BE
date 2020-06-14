package com.codurance.retropolis.services;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.responses.CardResponseObject;
import com.codurance.retropolis.responses.CardResponseObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

  @BeforeEach
  void setUp() {
    applicationCardService = new ApplicationCardService(userService, cardService,
        cardResponseObjectFactory);
  }

  @Test
  void creates_card_response_object() {
    Card card = new Card(CARD_ID, TEXT, COLUMN_ID, USER.getId(), emptyList());

    User author = new User(USER.getId(), USER.email, USER.username);
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, USER.email);
    CardResponseObject cardResponseObject = new CardResponseObject(card.getText(), card.getId(),
        card.getColumnId(), HAVE_VOTED, card.getVoters().size(), author.username);

    when(userService.findByEmail(requestObject.getEmail())).thenReturn(author);
    when(cardService.create(requestObject)).thenReturn(card);

    when(userService.findById(author.getId())).thenReturn(author);
    when(cardResponseObjectFactory.create(card, author.getId(), author.username))
        .thenReturn(cardResponseObject);

    CardResponseObject response = applicationCardService.create(requestObject);

    assertEquals(CARD_ID, response.getId());
    assertEquals(HAVE_VOTED, response.getHaveVoted());
    assertEquals(card.getVoters().size(), response.getTotalVoters());
    assertEquals(TEXT, response.getText());
    assertEquals(card.getColumnId(), response.getColumnId());
  }
}
