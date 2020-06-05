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

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.entities.ColumnType;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.exceptions.UnauthorizedException;
import com.codurance.retropolis.factories.UserFactory;
import com.codurance.retropolis.requests.NewBoardRequestObject;
import com.codurance.retropolis.services.BoardService;
import com.codurance.retropolis.services.LoginService;
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
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

  private final Long BOARD_ID = 1L;
  private final Long COLUMN_ID = 1L;
  private final String BOARD_TITLE = "test board";
  private final String SPECIFIC_BOARD_URL = "/boards/" + BOARD_ID;
  private final String BOARDS_URL = "/boards";
  private final String TOKEN = "SOMETOKEN";
  private final String USER_EMAIL = "john.doe@codurance.com";
  private final String USER_NAME = "John Doe";
  private final User USER = new User(USER_EMAIL, USER_NAME);
  private final Long NON_EXISTENT_BOARD_ID = 999L;

  @MockBean
  private BoardService boardService;

  @MockBean
  private UserFactory userFactory;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private LoginService loginService;

  private MockMvcWrapper mockMvcWrapper;

  @BeforeEach
  void setUp() {
    mockMvcWrapper = new MockMvcWrapper(context);
  }

  @Test
  void returns_a_board() throws Exception {
    when(userFactory.create(TOKEN)).thenReturn(USER);
    when(boardService.getBoard(USER, BOARD_ID))
        .thenReturn(new Board(BOARD_ID, BOARD_TITLE, emptyList()));
    String jsonResponse = mockMvcWrapper
        .getRequest(SPECIFIC_BOARD_URL, status().isOk(), getAuthHeader(TOKEN));
    Board boardResponse = mockMvcWrapper.buildObject(jsonResponse, Board.class);
    assertNotNull(boardResponse);
  }

  @Test
  void returns_board_with_columns() throws Exception {
    when(userFactory.create(TOKEN)).thenReturn(USER);
    List<Column> columns = List.of(new Column(COLUMN_ID, ColumnType.START));
    when(boardService.getBoard(USER, BOARD_ID))
        .thenReturn(new Board(BOARD_ID, BOARD_TITLE, columns));

    String jsonResponse = mockMvcWrapper
        .getRequest(SPECIFIC_BOARD_URL, status().isOk(), getAuthHeader(TOKEN));
    Board boardResponse = mockMvcWrapper.buildObject(jsonResponse, Board.class);

    assertEquals(columns.size(), boardResponse.getColumns().size());
    assertEquals(COLUMN_ID, boardResponse.getColumns().get(0).getId());
  }

  @Test
  void returns_board_with_columns_and_cards() throws Exception {
    String text = "hello";
    Long cardId = 1L;
    String userName = "John Doe";
    List<Card> cards = List.of(new Card(cardId, text, COLUMN_ID, userName, emptyList()));
    Column column = new Column(COLUMN_ID, ColumnType.START);
    column.setCards(cards);
    List<Column> columns = List.of(column);

    when(userFactory.create(TOKEN)).thenReturn(USER);
    when(boardService.getBoard(USER, BOARD_ID))
        .thenReturn(new Board(BOARD_ID, BOARD_TITLE, columns));

    String jsonResponse = mockMvcWrapper
        .getRequest(SPECIFIC_BOARD_URL, status().isOk(), getAuthHeader(TOKEN));
    Board boardResponse = mockMvcWrapper.buildObject(jsonResponse, Board.class);

    Column columnResponse = boardResponse.getColumns().get(0);
    assertEquals(cards.size(), columnResponse.getCards().size());
    Card cardResponse = columnResponse.getCards().get(0);
    assertEquals(text, cardResponse.getText());
    assertEquals(cardId, cardResponse.getId());
    assertEquals(COLUMN_ID, cardResponse.getColumnId());
    assertEquals(userName, cardResponse.getUsername());
  }

  @Test
  void returns_id_and_title_of_users_boards() throws Exception {
    when(userFactory.create(TOKEN)).thenReturn(USER);
    when(boardService.getUsersBoards(USER))
        .thenReturn(List.of(new Board(BOARD_ID, BOARD_TITLE, emptyList())));

    String jsonResponse = mockMvcWrapper
        .getRequest(BOARDS_URL, status().isOk(), getAuthHeader(TOKEN));
    List<Board> boards = objectMapper.readValue(jsonResponse, new TypeReference<>() {
    });

    assertEquals(1, boards.size());
    assertEquals(BOARD_TITLE, boards.get(0).getTitle());
    assertEquals(BOARD_ID, boards.get(0).getId());
  }

  @Test
  void returns_a_new_board() throws Exception {
    when(userFactory.create(TOKEN)).thenReturn(USER);
    NewBoardRequestObject requestObject = new NewBoardRequestObject(BOARD_TITLE, USER_EMAIL);
    given(boardService.createBoard(any(NewBoardRequestObject.class)))
        .willReturn(new Board(BOARD_ID, BOARD_TITLE, Collections.emptyList()));
    when(loginService.isAuthorized(USER_EMAIL, TOKEN)).thenReturn(true);

    String jsonResponse = mockMvcWrapper.postRequest(BOARDS_URL, asJsonString(requestObject), status().isCreated(),
        getAuthHeader(TOKEN));
    Board boardResponse = mockMvcWrapper.buildObject(jsonResponse, Board.class);

    assertEquals(BOARD_TITLE, boardResponse.getTitle());
    assertEquals(BOARD_ID, boardResponse.getId());
  }

  @Test
  public void returns_bad_request_when_board_does_not_exist() throws Exception {
    when(userFactory.create(TOKEN)).thenReturn(USER);
    doThrow(new BoardNotFoundException()).when(boardService).getBoard(USER, NON_EXISTENT_BOARD_ID);
    String jsonResponse = mockMvcWrapper
        .getRequest(BOARDS_URL + "/" + NON_EXISTENT_BOARD_ID, status().isBadRequest(),
            getAuthHeader(TOKEN));
    List<String> response = mockMvcWrapper.buildObject(jsonResponse);
    Assert.assertEquals("Board Id is not valid", response.get(0));
  }

  @Test
  void returns_bad_request_when_title_is_empty() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject("", "john.doe@codurance.com");
    String content = asJsonString(requestObject);
    String jsonResponse = mockMvcWrapper.postRequest(BOARDS_URL, content, status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    Assert.assertEquals("Title must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  void returns_bad_request_when_title_is_null() throws Exception {
    String jsonResponse = mockMvcWrapper
        .postRequest(BOARDS_URL, "{\"userEmail\":\"john.doe@codurance.com\"}", status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    Assert.assertEquals("Title cannot be empty", errorResponse.get(0));
  }

  @Test
  void returns_bad_request_when_userEmail_is_invalid() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject("test board", "invalid email");
    String content = asJsonString(requestObject);
    String jsonResponse = mockMvcWrapper.postRequest(BOARDS_URL, content, status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    Assert.assertEquals("Email is invalid", errorResponse.get(0));
  }

  @Test
  void returns_bad_request_when_email_is_null() throws Exception {
    String jsonResponse = mockMvcWrapper.postRequest(BOARDS_URL, "{\"title\":\"new board\"}", status().isBadRequest(),
        new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    Assert.assertEquals("Email is required", errorResponse.get(0));
  }

  @Test
  void throws_UnauthorizedException_when_email_does_not_match_the_token() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject(BOARD_TITLE, USER_EMAIL);
    when(loginService.isAuthorized(USER_EMAIL, TOKEN)).thenThrow(new UnauthorizedException());

    String jsonResponse = mockMvcWrapper
        .postRequest(BOARDS_URL, asJsonString(requestObject), status().isUnauthorized(), getAuthHeader(TOKEN));
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Unauthorized", errorResponse.get(0));
  }

}
