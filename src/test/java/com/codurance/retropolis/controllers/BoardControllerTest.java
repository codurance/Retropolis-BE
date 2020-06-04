package com.codurance.retropolis.controllers;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static com.codurance.retropolis.utils.MockMvcWrapper.getAuthHeader;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codurance.retropolis.config.web.GoogleTokenAuthenticator;
import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.entities.ColumnType;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import com.codurance.retropolis.services.BoardService;
import com.codurance.retropolis.utils.MockMvcWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

  public static final Long BOARD_ID = 1L;
  public static final Long COLUMN_ID = 1L;
  public static final String BOARD_TITLE = "test board";
  private static final String SPECIFIC_BOARD_URL = "/boards/" + BOARD_ID;
  private static final String BOARDS_URL = "/boards";
  public static final String TOKEN = "SOMETOKEN";
  public static final String USER_EMAIL = "john.doe@codurance.com";
  private static final Long NON_EXISTENT_BOARD_ID = 999L;

  @MockBean
  private BoardService boardService;

  @MockBean
  private GoogleTokenAuthenticator tokenAuthenticator;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  private MockMvcWrapper mockMvcWrapper;

  @BeforeEach
  void setUp() {
    mockMvcWrapper = new MockMvcWrapper(context);
  }

  @Test
  void returns_a_board() throws Exception {
    when(tokenAuthenticator.getEmail(TOKEN)).thenReturn(USER_EMAIL);
    when(boardService.getBoard(USER_EMAIL, BOARD_ID)).thenReturn(new Board(BOARD_ID, BOARD_TITLE, emptyList()));
    Board board = mockMvcWrapper.getRequest(SPECIFIC_BOARD_URL, status().isOk(), getAuthHeader(TOKEN), Board.class);
    assertNotNull(board);
  }

  @Test
  void returns_board_with_columns() throws Exception {
    when(tokenAuthenticator.getEmail(TOKEN)).thenReturn(USER_EMAIL);
    List<Column> columns = List.of(new Column(COLUMN_ID, ColumnType.START, emptyList()));
    when(boardService.getBoard(USER_EMAIL, BOARD_ID)).thenReturn(new Board(BOARD_ID, BOARD_TITLE, columns));

    Board board = mockMvcWrapper.getRequest(SPECIFIC_BOARD_URL, status().isOk(), getAuthHeader(TOKEN), Board.class);

    assertEquals(columns.size(), board.getColumns().size());
    assertEquals(COLUMN_ID, board.getColumns().get(0).getId());
  }

  @Test
  void returns_board_with_columns_and_cards() throws Exception {
    when(tokenAuthenticator.getEmail(TOKEN)).thenReturn(USER_EMAIL);
    String text = "hello";
    Long cardId = 1L;
    String userName = "John Doe";
    List<Card> cards = List.of(new Card(cardId, text, COLUMN_ID, userName));
    List<Column> columns = List.of(new Column(COLUMN_ID, ColumnType.START, cards));
    when(boardService.getBoard(USER_EMAIL, BOARD_ID)).thenReturn(new Board(BOARD_ID, BOARD_TITLE, columns));

    Board board = mockMvcWrapper.getRequest(SPECIFIC_BOARD_URL, status().isOk(), getAuthHeader(TOKEN), Board.class);

    Column columnResponse = board.getColumns().get(0);
    assertEquals(cards.size(), columnResponse.getCards().size());
    Card cardResponse = columnResponse.getCards().get(0);
    assertEquals(text, cardResponse.getText());
    assertEquals(cardId, cardResponse.getId());
    assertEquals(COLUMN_ID, cardResponse.getColumnId());
    assertEquals(userName, cardResponse.getUsername());
  }

  @Test
  void returns_id_and_title_of_users_boards() throws Exception {
    when(tokenAuthenticator.getEmail(TOKEN)).thenReturn(USER_EMAIL);
    when(boardService.getUsersBoards(USER_EMAIL))
        .thenReturn(List.of(new Board(BOARD_ID, BOARD_TITLE, emptyList())));

    List<Board> boards = requestUsersBoards();

    assertEquals(1, boards.size());
    assertEquals(BOARD_TITLE, boards.get(0).getTitle());
    assertEquals(BOARD_ID, boards.get(0).getId());
  }

  @Test
  void returns_a_new_board() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject(BOARD_TITLE, USER_EMAIL);
    given(boardService.createBoard(any(NewBoardRequestObject.class)))
        .willReturn(new Board(BOARD_ID, BOARD_TITLE, Collections.emptyList()));

    Board boardResponse = mockMvcWrapper.postRequest(BOARDS_URL, asJsonString(requestObject), status().isCreated(), Board.class);

    assertEquals(BOARD_TITLE, boardResponse.getTitle());
    assertEquals(BOARD_ID, boardResponse.getId());
  }

  @Test
  public void returns_bad_request_when_board_does_not_exist() throws Exception {
    when(tokenAuthenticator.getEmail(TOKEN)).thenReturn(USER_EMAIL);
    doThrow(new BoardNotFoundException()).when(boardService).getBoard(USER_EMAIL, NON_EXISTENT_BOARD_ID);
    List<String> response = mockMvcWrapper
        .getRequest(BOARDS_URL + "/" + NON_EXISTENT_BOARD_ID, status().isBadRequest(), getAuthHeader(TOKEN));
    Assert.assertEquals("Board Id is not valid", response.get(0));
  }

  @Test
  void returns_bad_request_when_title_is_empty() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject("", "john.doe@codurance.com");
    String content = asJsonString(requestObject);
    List<String> errorResponse = mockMvcWrapper.postRequest(BOARDS_URL, content, status().isBadRequest());
    Assert.assertEquals("Title must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  void returns_bad_request_when_title_is_null() throws Exception {
    List<String> errorResponse = mockMvcWrapper
        .postRequest(BOARDS_URL, "{\"userEmail\":\"john.doe@codurance.com\"}", status().isBadRequest());
    Assert.assertEquals("Title cannot be empty", errorResponse.get(0));
  }

  @Test
  void returns_bad_request_when_userEmail_is_invalid() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject("test board", "invalid email");
    String content = asJsonString(requestObject);
    List<String> errorResponse = mockMvcWrapper.postRequest(BOARDS_URL, content, status().isBadRequest());
    Assert.assertEquals("Email is invalid", errorResponse.get(0));
  }

  @Test
  void returns_bad_request_when_email_is_null() throws Exception {
    List<String> errorResponse = mockMvcWrapper.postRequest(BOARDS_URL, "{\"title\":\"new board\"}", status().isBadRequest());
    Assert.assertEquals("Email is required", errorResponse.get(0));
  }

  //TODO #2. Refactor: Same with #1 (duplicated)
  private List<Board> requestUsersBoards() throws Exception {
    MvcResult httpResponse = mockMvc
        .perform(MockMvcRequestBuilders.get(BOARDS_URL).header(HttpHeaders.AUTHORIZATION, TOKEN))
        .andExpect(status().isOk()).andReturn();
    String contentAsString = httpResponse.getResponse().getContentAsString();
    return objectMapper.readValue(contentAsString, new TypeReference<>() {
    });
  }

}
