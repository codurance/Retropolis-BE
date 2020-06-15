package com.codurance.retropolis.controllers;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static com.codurance.retropolis.utils.MockMvcWrapper.getAuthHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.CardNotFoundException;
import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import com.codurance.retropolis.requests.UpdateCardRequestObject;
import com.codurance.retropolis.responses.CardResponseObject;
import com.codurance.retropolis.responses.CardUpdatedTextResponseObject;
import com.codurance.retropolis.services.ApplicationCardService;
import com.codurance.retropolis.services.CardService;
import com.codurance.retropolis.services.LoginService;
import com.codurance.retropolis.utils.MockMvcWrapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(CardController.class)
public class CardControllerTest {

  public final Long NON_EXISTENT_CARD_ID = 999L;
  public final Long CARD_ID = 1L;
  public final Long COLUMN_ID = 2L;
  public final String TEXT = "hello";
  public final HttpHeaders EMPTY_HEADERS = new HttpHeaders();
  private final String URL = "/cards";
  private final String TOKEN = "SOMETOKEN";
  private final Boolean HAVE_VOTED = false;
  private final Integer TOTAL_VOTERS = 0;
  private final User USER = new User(3L, "john.doe@codurance.com", "John Doe");
  private final String VOTER_EMAIL = "jane.doe@codurance.com";

  @MockBean
  private CardService cardService;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private LoginService loginService;

  @MockBean
  private ApplicationCardService applicationCardService;

  private MockMvcWrapper mockMvcWrapper;

  @BeforeEach
  void setUp() {
    mockMvcWrapper = new MockMvcWrapper(context);
  }

  @Test
  public void post_cards_should_return_back_cardResponseObject() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, USER.email);
    when(cardService.create(any(NewCardRequestObject.class)))
        .thenReturn(new Card(TEXT, COLUMN_ID, USER.getId(), Collections.EMPTY_LIST));

    when(applicationCardService.create(any(NewCardRequestObject.class)))
        .thenReturn(new CardResponseObject(TEXT, CARD_ID, COLUMN_ID, HAVE_VOTED, TOTAL_VOTERS,
            USER.username));
    when(loginService.isAuthorized(USER.email, TOKEN)).thenReturn(true);

    String jsonResponse = mockMvcWrapper
        .postRequest(URL, asJsonString(requestObject), status().isCreated(), getAuthHeader(TOKEN));
    CardResponseObject cardResponseObject = mockMvcWrapper
        .buildObject(jsonResponse, CardResponseObject.class);

    assertEquals(TEXT, cardResponseObject.getText());
    assertEquals(CARD_ID, cardResponseObject.getId());
    assertEquals(COLUMN_ID, cardResponseObject.getColumnId());
    assertEquals(HAVE_VOTED, cardResponseObject.getHaveVoted());
    assertEquals(TOTAL_VOTERS, cardResponseObject.getTotalVoters());
    assertEquals(USER.username, cardResponseObject.getAuthor());
  }

  @Test
  public void delete_card_should_return_200_status_code() throws Exception {
    mockMvcWrapper.deleteRequest(URL + "/" + CARD_ID, status().isOk());
    verify(applicationCardService).delete(CARD_ID);
  }

  @Test
  public void update_card_with_new_text_should_return_card_with_updated_text() throws Exception {
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject(TEXT);
    when(applicationCardService.updateText(any(), any(UpdateCardRequestObject.class)))
        .thenReturn(new CardUpdatedTextResponseObject(CARD_ID, TEXT, COLUMN_ID));

    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/" + CARD_ID, asJsonString(requestObject), status().isOk());
    CardUpdatedTextResponseObject cardResponse = mockMvcWrapper
        .buildObject(jsonResponse, CardUpdatedTextResponseObject.class);

    assertEquals(TEXT, cardResponse.getText());
  }

  @Test
  public void returns_bad_request_on_delete_when_card_does_not_exist() throws Exception {
    doThrow(new CardNotFoundException()).when(applicationCardService).delete(NON_EXISTENT_CARD_ID);
    String jsonResponse = mockMvcWrapper.deleteRequest(URL + "/" + NON_EXISTENT_CARD_ID, status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Card Id is not valid", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_when_column_is_not_found() throws Exception {
    when(loginService.isAuthorized(USER.email, TOKEN)).thenReturn(true);
    when(applicationCardService.create(any(NewCardRequestObject.class)))
        .thenThrow(new ColumnNotFoundException());
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, USER.email);

    String jsonResponse = mockMvcWrapper
        .postRequest(URL, asJsonString(requestObject), status().isBadRequest(), getAuthHeader(TOKEN));

    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Column Id is not valid", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_text_is_empty_on_post_card() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject("", COLUMN_ID, USER.email);
    String content = asJsonString(requestObject);

    String jsonResponse = mockMvcWrapper.postRequest(URL, content, status().isBadRequest(), EMPTY_HEADERS);
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);

    assertEquals("Text must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_columnId_is_null() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, null, USER.email);
    String jsonResponse = mockMvcWrapper.postRequest(URL, asJsonString(requestObject),
        status().isBadRequest(), EMPTY_HEADERS);
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Column id cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_text_is_sent_on_post_card() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(null, COLUMN_ID, USER.email);
    String jsonResponse = mockMvcWrapper.postRequest(URL, asJsonString(requestObject), status().isBadRequest(), EMPTY_HEADERS);
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_email_is_sent_on_create() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, null);
    String jsonResponse = mockMvcWrapper.postRequest(URL, asJsonString(requestObject), status().isBadRequest(), EMPTY_HEADERS);
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is required", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_text_is_empty_on_edit_text_card() throws Exception {
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject("");
    String content = asJsonString(requestObject);
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1", content, status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_text_is_sent_on_updateText() throws Exception {
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject(null);
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text cannot be empty", errorResponse.get(0));
  }

  @Test
  void update_card_vote_with_email_and_add_vote_should_return_card_with_voter() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(VOTER_EMAIL, true);
    Boolean haveVoted = true;
    Integer expectedVoteCount = 1;
    when(applicationCardService.addUpvote(any(Long.class), any(UpVoteRequestObject.class)))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));

    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/" + CARD_ID + "/vote", asJsonString(requestObject), status().isOk());
    CardResponseObject cardResponseObject = mockMvcWrapper.buildObject(jsonResponse, CardResponseObject.class);

    assertEquals(expectedVoteCount, cardResponseObject.getTotalVoters());
    assertTrue(cardResponseObject.getHaveVoted());
  }

  @Test
  public void return_bad_request_when_email_is_empty_on_upvote() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(null, true);
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1/vote", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is required", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_email_is_invalid_on_upvote() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject("invalid mail", true);
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1/vote", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is invalid", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_addVote_parameter_missing() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(VOTER_EMAIL, null);
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1/vote", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("AddVote parameter is required", errorResponse.get(0));
  }

  @Test
  void remove_card_vote_with_voter_email_should_return_card_without_voter() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(VOTER_EMAIL, false);
    when(applicationCardService.removeUpvote(any(Long.class), any(UpVoteRequestObject.class)))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));

    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/" + CARD_ID + "/vote", asJsonString(requestObject), status().isOk());
    CardResponseObject cardResponseObject = mockMvcWrapper.buildObject(jsonResponse, CardResponseObject.class);

    assertEquals(TOTAL_VOTERS, cardResponseObject.getTotalVoters());
    assertFalse(cardResponseObject.getHaveVoted());
  }
}
