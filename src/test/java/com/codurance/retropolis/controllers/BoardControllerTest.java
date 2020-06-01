package com.codurance.retropolis.controllers;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codurance.retropolis.config.GoogleTokenAuthenticator;
import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.Column;
import com.codurance.retropolis.services.BoardService;
import com.codurance.retropolis.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

  public static final Long BOARD_ID = 1L;
  public static final Long COLUMN_ID = 1L;
  public static final String BOARD_TITLE = "test board";
  private static final String URL = "/boards/" + BOARD_ID;
  public static final String TOKEN = "SOMETOKEN";

  @MockBean
  private BoardService boardService;

  @MockBean
  private UserService userService;

  @MockBean
  private GoogleTokenAuthenticator tokenAuthenticator;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() throws GeneralSecurityException, IOException {
    String email = "john.doe@codurance.com";
    when(tokenAuthenticator.getEmail(TOKEN)).thenReturn(email);
  }

  @Test
  void returns_a_board() throws Exception {
    when(boardService.getBoard(BOARD_ID)).thenReturn(new Board(BOARD_ID, BOARD_TITLE, emptyList()));
    Board board = requestBoard();
    assertNotNull(board);
  }

  @Test
  void returns_board_with_columns() throws Exception {
    List<Column> columns = List.of(new Column(COLUMN_ID, "start", emptyList()));
    when(boardService.getBoard(BOARD_ID)).thenReturn(new Board(BOARD_ID, BOARD_TITLE, columns));

    Board board = requestBoard();

    assertEquals(columns.size(), board.getColumns().size());
    assertEquals(COLUMN_ID, board.getColumns().get(0).getId());
  }

  @Test
  void returns_board_with_columns_and_cards() throws Exception {
    String text = "hello";
    long cardId = 1;
    String userName = "John Doe";
    List<Card> cards = List.of(new Card(cardId, text, COLUMN_ID, userName));
    List<Column> columns = List.of(new Column(COLUMN_ID, "start", cards));
    when(boardService.getBoard(BOARD_ID)).thenReturn(new Board(BOARD_ID, BOARD_TITLE, columns));

    Board board = requestBoard();

    Column columnResponse = board.getColumns().get(0);
    assertEquals(cards.size(), columnResponse.getCards().size());
    Card cardResponse = columnResponse.getCards().get(0);
    assertEquals(text, cardResponse.getText());
    assertEquals(cardId, cardResponse.getId());
    assertEquals(COLUMN_ID, cardResponse.getColumnId());
    assertEquals(userName, cardResponse.getUsername());
  }

  private Board requestBoard() throws Exception {
    MvcResult httpResponse = mockMvc.perform(MockMvcRequestBuilders.get(URL).header(HttpHeaders.AUTHORIZATION, TOKEN))
        .andExpect(status().isOk()).andReturn();
    String contentAsString = httpResponse.getResponse().getContentAsString();
    return objectMapper.readValue(contentAsString, new TypeReference<>() {
    });
  }
}
