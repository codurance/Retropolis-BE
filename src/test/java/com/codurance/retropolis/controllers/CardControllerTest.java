package com.codurance.retropolis.controllers;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static com.codurance.retropolis.utils.MockMvcWrapper.getAuthHeader;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.exceptions.CardNotFoundException;
import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import com.codurance.retropolis.requests.UpdateCardRequestObject;
import com.codurance.retropolis.responses.CardResponseObject;
import com.codurance.retropolis.services.CardService;
import com.codurance.retropolis.services.LoginService;
import com.codurance.retropolis.utils.MockMvcWrapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(CardController.class)
public class CardControllerTest {

  public static final Long NON_EXISTENT_CARD_ID = 999L;
  public static final Long CARD_ID = 1L;
  public static final Long COLUMN_ID = 1L;
  private static final String URL = "/cards";
  public static final String TEXT = "hello";
  public static final Long USER_ID = 1L;
  private static final Boolean HAVE_VOTED = false;
  private static final Integer TOTAL_VOTERS = 0;
  private static final String USERNAME = "John Doe";
  private final String TOKEN = "SOMETOKEN";
  private final String USER_EMAIL = "john.doe@codurance.com";

  @MockBean
  private CardService cardService;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private LoginService loginService;

  private MockMvcWrapper mockMvcWrapper;

  @BeforeEach
  void setUp() {
    mockMvcWrapper = new MockMvcWrapper(context);
  }

  @Test
  public void post_cards_should_return_back_card_instance_with_id_in_response() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, USER_EMAIL);
    given(cardService.addCard(any(NewCardRequestObject.class)))
        .willReturn(
            new CardResponseObject(TEXT, CARD_ID, COLUMN_ID, HAVE_VOTED, TOTAL_VOTERS, USERNAME));
    when(loginService.isAuthorized(USER_EMAIL, TOKEN)).thenReturn(true);

    String jsonResponse = mockMvcWrapper
        .postRequest(URL, asJsonString(requestObject), status().isCreated(), getAuthHeader(TOKEN));
    CardResponseObject cardResponseObject = mockMvcWrapper
        .buildObject(jsonResponse, CardResponseObject.class);

    assertEquals(TEXT, cardResponseObject.getText());
    assertEquals(CARD_ID, cardResponseObject.getCardId());
    assertEquals(COLUMN_ID, cardResponseObject.getCardId());
    assertEquals(HAVE_VOTED, cardResponseObject.getHaveVoted());
    assertEquals(TOTAL_VOTERS, cardResponseObject.getTotalVoters());
    assertEquals(USERNAME, cardResponseObject.getAuthor());
  }

  @Test
  public void delete_card_should_return_200_status_code() throws Exception {
    mockMvcWrapper.deleteRequest(URL + "/" + CARD_ID, status().isOk());

    verify(cardService).delete(CARD_ID);
  }

  @Test
  public void update_card_with_new_text_should_return_updated_card() throws Exception {
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject(TEXT);

    given(cardService.update(any(), any(UpdateCardRequestObject.class)))
        .willReturn(new Card(CARD_ID, TEXT, COLUMN_ID, USER_ID, emptyList()));

    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/" + CARD_ID, asJsonString(requestObject), status().isOk());
    Card cardResponse = mockMvcWrapper.buildObject(jsonResponse, Card.class);

    assertEquals(TEXT, cardResponse.getText());
  }

  @Test
  void update_card_vote_with_username_should_return_card_with_voter() throws Exception {
    Long voter = 2L;
    String voterEmail = "jane.doe@codurance.com";
    UpVoteRequestObject requestObject = new UpVoteRequestObject(voterEmail);

    given(cardService.updateVotes(any(), any(UpVoteRequestObject.class)))
        .willReturn(new CardResponseObject(TEXT, CARD_ID, COLUMN_ID, HAVE_VOTED, 1, USERNAME));

    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/" + CARD_ID + "/vote", asJsonString(requestObject), status().isOk());
    CardResponseObject cardResponseObject = mockMvcWrapper.buildObject(jsonResponse, CardResponseObject.class);

    assertEquals(1, cardResponseObject.getTotalVoters());
    assertFalse(cardResponseObject.getHaveVoted());
  }

  @Test
  public void returns_bad_request_on_delete_when_card_does_not_exist() throws Exception {
    doThrow(new CardNotFoundException()).when(cardService).delete(NON_EXISTENT_CARD_ID);
    String jsonResponse = mockMvcWrapper.deleteRequest(URL + "/" + NON_EXISTENT_CARD_ID, status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Card Id is not valid", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_when_column_is_not_found() throws Exception {
    when(loginService.isAuthorized(USER_EMAIL, TOKEN)).thenReturn(true);
    given(cardService.addCard(any(NewCardRequestObject.class))).willThrow(new ColumnNotFoundException());
    NewCardRequestObject requestObject = new NewCardRequestObject("hello", 1L, USER_EMAIL);
    String jsonResponse = mockMvcWrapper
        .postRequest(URL, asJsonString(requestObject), status().isBadRequest(), getAuthHeader(TOKEN));
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Column Id is not valid", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_text_is_empty_on_post_card() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject("", COLUMN_ID, USER_EMAIL);
    String content = asJsonString(requestObject);
    String jsonResponse = mockMvcWrapper.postRequest(URL, content, status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_columnId_is_null() throws Exception {
    String jsonResponse = mockMvcWrapper.postRequest(URL, "{\"text\":\"hello\",\"email\":\"john.doe@codurance.com\"}",
        status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Column id cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_text_is_sent_on_post_card() throws Exception {
    String jsonResponse = mockMvcWrapper.postRequest(URL, "{\"columnId\":\"1\",\"email\":\"john.doe@codurance.com\"}",
        status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text cannot be empty", errorResponse.get(0));
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
  public void return_bad_request_when_no_text_is_sent_on_edit_text_card() throws Exception {
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1", "{}",
        status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_username_is_sent() throws Exception {
    String jsonResponse = mockMvcWrapper
        .postRequest(URL, "{\"text\":\"hello\",\"columnId\":\"1\"}", status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is required", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_email_is_empty_on_add_vote() throws Exception {
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1/vote",
        "{}", status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is required", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_email_is_invalid() throws Exception {
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1/vote",
        "{\"email\":\"hello\"}", status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is invalid", errorResponse.get(0));
  }

}
