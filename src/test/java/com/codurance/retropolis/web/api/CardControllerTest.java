package com.codurance.retropolis.web.api;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static com.codurance.retropolis.utils.MockMvcWrapper.getAuthHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codurance.retropolis.applicationservices.ApplicationCardService;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.CardNotFoundException;
import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.services.LoginService;
import com.codurance.retropolis.utils.MockMvcWrapper;
import com.codurance.retropolis.web.requests.NewCardRequestObject;
import com.codurance.retropolis.web.requests.UpVoteRequestObject;
import com.codurance.retropolis.web.requests.UpdateCardRequestObject;
import com.codurance.retropolis.web.responses.CardResponseObject;
import com.codurance.retropolis.web.responses.CardUpdatedTextResponseObject;
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
  public void returns_card_response_object_when_card_is_created() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, USER.email);

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
  public void returns_ok_response_when_card_is_deleted() throws Exception {
    mockMvcWrapper.deleteRequest(URL + "/" + CARD_ID, status().isOk());
    verify(applicationCardService).delete(CARD_ID);
  }

  @Test
  public void returns_updated_card_response_when_card_is_updated_with_test() throws Exception {
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
  public void returns_bad_request_when_text_is_empty_on_post_card() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject("", COLUMN_ID, USER.email);
    String content = asJsonString(requestObject);

    String jsonResponse = mockMvcWrapper
        .postRequest(URL, content, status().isBadRequest(), EMPTY_HEADERS);
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);

    assertEquals("Text must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_when_column_id_is_null() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, null, USER.email);
    String jsonResponse = mockMvcWrapper.postRequest(URL, asJsonString(requestObject),
        status().isBadRequest(), EMPTY_HEADERS);
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Column id cannot be empty", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_when_no_text_is_sent_on_post_card() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(null, COLUMN_ID, USER.email);
    String jsonResponse = mockMvcWrapper
        .postRequest(URL, asJsonString(requestObject), status().isBadRequest(), EMPTY_HEADERS);
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text cannot be empty", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_when_no_email_is_sent_on_create() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject(TEXT, COLUMN_ID, null);
    String jsonResponse = mockMvcWrapper
        .postRequest(URL, asJsonString(requestObject), status().isBadRequest(), EMPTY_HEADERS);
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is required", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_when_text_is_empty_on_edit_text_card() throws Exception {
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject("");
    String content = asJsonString(requestObject);
    String jsonResponse = mockMvcWrapper.patchRequest(URL + "/1", content, status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_when_no_text_is_sent_on_update_text() throws Exception {
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject(null);
    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/1", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Text cannot be empty", errorResponse.get(0));
  }

  @Test
  void returns_ok_response_when_upvote_sent_with_email_address() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(VOTER_EMAIL, true);
    when(applicationCardService.addUpvote(any(Long.class), any(UpVoteRequestObject.class)))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    mockMvcWrapper
        .patchRequest(URL + "/" + CARD_ID + "/vote", asJsonString(requestObject), status().isOk());
  }

  @Test
  public void returns_bad_request_if_email_is_empty_when_adding_upvote() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(null, true);
    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/1/vote", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is required", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_if_email_is_invalid_when_adding_upvote() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject("invalid email", true);
    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/1/vote", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is invalid", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_when_add_vote_parameter_missing() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(VOTER_EMAIL, null);
    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/1/vote", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("AddVote parameter is required", errorResponse.get(0));
  }

  @Test
  void returns_ok_response_when_upvote_removed_with_email_address() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(VOTER_EMAIL, false);

    ResponseEntity<HttpStatus> responseEntity = new ResponseEntity<>(HttpStatus.OK);
    when(applicationCardService.removeUpvote(any(Long.class), any(UpVoteRequestObject.class)))
        .thenReturn(responseEntity);

    mockMvcWrapper
        .patchRequest(URL + "/" + CARD_ID + "/vote", asJsonString(requestObject), status().isOk());
  }

  @Test
  public void returns_bad_request_if_email_is_empty_when_removing_upvote() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject(null, true);
    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/1/vote", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is required", errorResponse.get(0));
  }

  @Test
  public void returns_bad_request_if_email_is_invalid_when_removing_upvote() throws Exception {
    UpVoteRequestObject requestObject = new UpVoteRequestObject("invalid mail", true);
    String jsonResponse = mockMvcWrapper
        .patchRequest(URL + "/1/vote", asJsonString(requestObject), status().isBadRequest());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Email is invalid", errorResponse.get(0));
  }
}
