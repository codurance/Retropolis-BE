package com.codurance.retropolis.controllers;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codurance.retropolis.exceptions.ColumnNotFoundException;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.services.CardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(CardController.class)
public class CardControllerTest {

  private static final String URL = "/cards";
  public static final String NULL = null;
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CardService cardService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void post_cards_should_return_back_card_instance_with_id_in_response() throws Exception {
    int cardId = 1;
    int columnId = 1;
    String cardText = "hello";
    String userName = "John Doe";
    NewCardRequestObject requestObject = new NewCardRequestObject(cardText, columnId, userName);
    given(cardService.addCard(any(NewCardRequestObject.class))).willReturn(new Card(cardId, cardText, columnId, userName));

    MvcResult response = performHttpRequest(asJsonString(requestObject), status().isCreated());
    String responseBody = response.getResponse().getContentAsString();
    Card cardResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
    });

    assertEquals(cardText, cardResponse.getText());
    assertEquals(cardId, cardResponse.getId());
    assertEquals(columnId, cardResponse.getColumnId());
    assertEquals(userName, cardResponse.getUserName());
  }

  @Test
  public void returns_bad_request_when_column_is_not_found() throws Exception {
    given(cardService.addCard(any(NewCardRequestObject.class))).willThrow(new ColumnNotFoundException("Column Id is not valid"));
    NewCardRequestObject requestObject = new NewCardRequestObject("hello", 1, "John Doe");

    MvcResult response = performHttpRequest(asJsonString(requestObject), status().isBadRequest());
    String responseBody = response.getResponse().getContentAsString();
    List<String> cardResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
    });

    assertEquals("Column Id is not valid", cardResponse.get(0));
  }

  @Test
  public void return_bad_request_when_text_is_empty() throws Exception {
    NewCardRequestObject requestObject = new NewCardRequestObject("", 1, "John Doe");
    String content = asJsonString(requestObject);
    MvcResult response = performHttpRequest(content, status().isBadRequest());

    String responseBody = response.getResponse().getContentAsString();
    List<String> errorResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
    });
    assertEquals("Text must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_columnId_is_null() throws Exception {
    MvcResult response = performHttpRequest("{\"text\":\"hello\"}", status().isBadRequest());
    String responseBody = response.getResponse().getContentAsString();
    List<String> errorResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
    });
    assertEquals("Column id cannot be empty", errorResponse.get(0));
  }

  @Test
  public void return_bad_request_when_no_text_is_sent() throws Exception {
    MvcResult response = performHttpRequest("{\"columnId\":\"1\"}", status().isBadRequest());
    String responseBody = response.getResponse().getContentAsString();
    List<String> errorResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
    });
    assertEquals("Text cannot be empty", errorResponse.get(0));
  }


  private MvcResult performHttpRequest(String content, ResultMatcher response) throws Exception {
    return mockMvc.perform(MockMvcRequestBuilders.post(URL)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(response).andReturn();
  }
}
