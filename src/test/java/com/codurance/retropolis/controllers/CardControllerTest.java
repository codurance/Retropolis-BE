package com.codurance.retropolis.controllers;

import static com.codurance.retropolis.utils.Convert.asJsonString;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(CardController.class)
public class CardControllerTest {

  public static final Long NON_EXISTENT_CARD_ID = 999L;
  public static final Long CARD_ID = 1L;
  public static final Long COLUMN_ID = 1L;
  private static final String URL = "/cards";
  public static final String TEXT = "hello";
  public static final String USERNAME = "John Doe";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CardService cardService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void post_cards_should_return_back_card_instance_with_id_in_response() throws Exception {
    String TEXT = "hello";
    NewCardRequestObject requestObject = new NewCardRequestObject(CardControllerTest.TEXT, COLUMN_ID, USERNAME);
    given(cardService.addCard(any(NewCardRequestObject.class)))
        .willReturn(new Card(CARD_ID, CardControllerTest.TEXT, COLUMN_ID, USERNAME));

    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(URL)
        .content(asJsonString(requestObject))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated()).andReturn();

    String responseBody = response.getResponse().getContentAsString();
    Card cardResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
    });

    assertEquals(CardControllerTest.TEXT, cardResponse.getText());
    assertEquals(CARD_ID, cardResponse.getId());
    assertEquals(COLUMN_ID, cardResponse.getColumnId());
    assertEquals(USERNAME, cardResponse.getUsername());
  }

  @Test
  public void delete_card_should_return_200_status_code() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/" + CARD_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    verify(cardService).delete(CARD_ID);
  }

  @Test
  public void update_card_with_new_text_should_return_updated_card() throws Exception {
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject(TEXT);

    given(cardService.update(any(), any(UpdateCardRequestObject.class)))
        .willReturn(new Card(CARD_ID, TEXT, COLUMN_ID, USERNAME));

    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.patch(URL + "/" + CARD_ID)
        .content(asJsonString(requestObject))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String responseBody = response.getResponse().getContentAsString();
    Card cardResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
    });

    assertEquals(TEXT, cardResponse.getText());
  }

  @Test
  void update_card_vote_with_username_should_return_card_with_voter() throws Exception {
    String voter = "tom";
    UpVoteRequestObject requestObject = new UpVoteRequestObject(voter, true);

    given(cardService.updateVotes(any(), any(UpVoteRequestObject.class)))
        .willReturn(new Card(CARD_ID, TEXT, COLUMN_ID, USERNAME, Collections.singletonList(voter)));

    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.patch(URL + "/" + CARD_ID + "/vote")
        .content(asJsonString(requestObject))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    String responseBody = response.getResponse().getContentAsString();
    Card cardResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
    });

    assertEquals(1, cardResponse.getVoters().size());
    assertEquals(voter, cardResponse.getVoters().get(0));
  }

  @Test
  public void returns_bad_request_on_delete_when_card_does_not_exist() throws Exception {
    doThrow(new CardNotFoundException()).when(cardService).delete(NON_EXISTENT_CARD_ID);
    List<String> response = performHttpDeleteRequest(status().isBadRequest(), URL + "/" + NON_EXISTENT_CARD_ID);
    assertEquals("Card Id is not valid", response.get(0));
  }

  @Test
  public void returns_bad_request_when_column_is_not_found() throws Exception {
    given(cardService.addCard(any(NewCardRequestObject.class))).willThrow(new ColumnNotFoundException());
    NewCardRequestObject requestObject = new NewCardRequestObject("hello", 1L, "John Doe");

    List<String> cardResponse = performHttpPostRequest(asJsonString(requestObject), status().isBadRequest());
    assertEquals("Column Id is not valid", cardResponse.get(0));
  }

  @Test
  public void return_bad_request_when_text_is_empty_on_post_card() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject("", 1L, "John Doe");
    String content = asJsonString(requestObject);
    List<String> errorResponse = performHttpPostRequest(content, status().isBadRequest());
    assertEquals("Text must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_columnId_is_null() throws Exception {
    List<String> errorResponse = performHttpPostRequest("{\"text\":\"hello\",\"username\":\"John Doe\"}",
        status().isBadRequest());
    assertEquals("Column id cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_text_is_sent_on_post_card() throws Exception {
    List<String> errorResponse = performHttpPostRequest("{\"columnId\":\"1\",\"username\":\"John Doe\"}",
        status().isBadRequest());
    assertEquals("Text cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_text_is_empty_on_edit_text_card() throws Exception {
    UpdateCardRequestObject requestObject = new UpdateCardRequestObject("");
    String content = asJsonString(requestObject);
    List<String> errorResponse = performHttpPatchRequest(content, status().isBadRequest(), URL + "/1");
    assertEquals("Text must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_text_is_sent_on_edit_text_card() throws Exception {
    List<String> errorResponse = performHttpPatchRequest("{}",
        status().isBadRequest(), URL + "/1");
    assertEquals("Text cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_username_is_sent() throws Exception {
    List<String> errorResponse = performHttpPostRequest("{\"text\":\"hello\",\"columnId\":\"1\"}", status().isBadRequest());
    assertEquals("Username cannot be null", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_username_is_empty_on_add_vote() throws Exception {
    List<String> errorResponse = performHttpPatchRequest("{\"addVote\":\"true\"}", status().isBadRequest(), URL + "/1/vote");
    assertEquals("Username cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_addVote_is_empty_on_add_vote() throws Exception {
    List<String> errorResponse = performHttpPatchRequest("{\"username\":\"John Doe\"}", status().isBadRequest(), URL + "/1/vote");
    assertEquals("addVote cannot be empty", errorResponse.get(0));
  }

  //TODO Refactor
  private <T> T performHttpPostRequest(String content, ResultMatcher response) throws Exception {
    MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post(URL);
    String responseBody = mockMvc.perform(post
        .content(content)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(response).andReturn().getResponse().getContentAsString();

    return objectMapper.readValue(responseBody, new TypeReference<>() {
    });
  }

  private <T> T performHttpPatchRequest(String content, ResultMatcher response, String url) throws Exception {
    MockHttpServletRequestBuilder post = MockMvcRequestBuilders.patch(url);
    String responseBody = mockMvc.perform(post
        .content(content)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(response).andReturn().getResponse().getContentAsString();

    return objectMapper.readValue(responseBody, new TypeReference<>() {
    });
  }

  private <T> T performHttpDeleteRequest(ResultMatcher response, String url) throws Exception {
    String responseBody = mockMvc.perform(MockMvcRequestBuilders.delete(url)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(response).andReturn().getResponse().getContentAsString();

    return objectMapper.readValue(responseBody, new TypeReference<>() {
    });
  }

}
