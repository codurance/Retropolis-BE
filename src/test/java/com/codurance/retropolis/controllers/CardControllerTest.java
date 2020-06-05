package com.codurance.retropolis.controllers;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.exceptions.CardNotFoundException;
import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.requests.UpVoteRequestObject;
import com.codurance.retropolis.requests.UpdateCardRequestObject;
import com.codurance.retropolis.services.CardService;
import com.codurance.retropolis.utils.MockMvcWrapper;
import java.util.Collections;
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
  public static final String USERNAME = "John Doe";

  @MockBean
  private CardService cardService;

  @Autowired
  private WebApplicationContext context;

  private MockMvcWrapper mockMvcWrapper;

  @BeforeEach
  void setUp() {
    mockMvcWrapper = new MockMvcWrapper(context);
  }

  @Test
  public void post_cards_should_return_back_card_instance_with_id_in_response() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, USERNAME);
    given(cardService.addCard(any(NewCardRequestObject.class)))
        .willReturn(new Card(CARD_ID, TEXT, COLUMN_ID, USERNAME, emptyList()));

    String jsonResponse = mockMvcWrapper
        .postRequest(URL, asJsonString(requestObject), status().isCreated(), new HttpHeaders());
    Card cardResponse = mockMvcWrapper.buildObject(jsonResponse, Card.class);

    assertEquals(TEXT, cardResponse.getText());
    assertEquals(CARD_ID, cardResponse.getId());
    assertEquals(COLUMN_ID, cardResponse.getColumnId());
    assertEquals(USERNAME, cardResponse.getUsername());
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
        .willReturn(new Card(CARD_ID, TEXT, COLUMN_ID, USERNAME, emptyList()));

    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/" + CARD_ID, asJsonString(requestObject), status().isOk());
    Card cardResponse = mockMvcWrapper.buildObject(jsonResponse, Card.class);

    assertEquals(TEXT, cardResponse.getText());
  }

  @Test
  void update_card_vote_with_username_should_return_card_with_voter() throws Exception {
    String voter = "tom";
    UpVoteRequestObject requestObject = new UpVoteRequestObject(voter);

    given(cardService.updateVotes(any(), any(UpVoteRequestObject.class)))
        .willReturn(new Card(CARD_ID, TEXT, COLUMN_ID, USERNAME, Collections.singletonList(voter)));

    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/" + CARD_ID + "/vote", asJsonString(requestObject), status().isOk());
    Card cardResponse = mockMvcWrapper.buildObject(jsonResponse, Card.class);

    assertEquals(1, cardResponse.getVoters().size());
    assertEquals(voter, cardResponse.getVoters().get(0));
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
    given(cardService.addCard(any(NewCardRequestObject.class))).willThrow(new ColumnNotFoundException());
    NewCardRequestObject requestObject = new NewCardRequestObject("hello", 1L, "John Doe");
    String jsonResponse = mockMvcWrapper
        .postRequest(URL, asJsonString(requestObject), status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Column Id is not valid", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_text_is_empty_on_post_card() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject("", 1L, "John Doe");
    String content = asJsonString(requestObject);
    String jsonResponse = mockMvcWrapper.postRequest(URL, content, status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_columnId_is_null() throws Exception {
    String jsonResponse = mockMvcWrapper.postRequest(URL, "{\"text\":\"hello\",\"username\":\"John Doe\"}",
        status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Column id cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_text_is_sent_on_post_card() throws Exception {
    String jsonResponse = mockMvcWrapper.postRequest(URL, "{\"columnId\":\"1\",\"username\":\"John Doe\"}",
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
    assertEquals("Username cannot be null", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_username_is_empty_on_add_vote() throws Exception {
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1/vote",
        "{}", status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Username cannot be empty", errorResponse.get(0));
  }

}
