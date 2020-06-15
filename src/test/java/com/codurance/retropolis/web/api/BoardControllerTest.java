package com.codurance.retropolis.web.api;

import static com.codurance.retropolis.utils.Convert.asJsonString;
import static com.codurance.retropolis.utils.MockMvcWrapper.getAuthHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codurance.retropolis.applicationservices.ApplicationBoardService;
import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.ColumnType;
import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.BoardNotFoundException;
import com.codurance.retropolis.exceptions.UnauthorizedException;
import com.codurance.retropolis.factories.UserFactory;
import com.codurance.retropolis.services.LoginService;
import com.codurance.retropolis.utils.MockMvcWrapper;
import com.codurance.retropolis.web.requests.NewBoardRequestObject;
import com.codurance.retropolis.web.responses.BoardResponseObject;
import com.codurance.retropolis.web.responses.CardResponseObject;
import com.codurance.retropolis.web.responses.ColumnResponseObject;
import com.codurance.retropolis.web.responses.UserBoardResponseObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
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
  private final Long NON_EXISTENT_BOARD_ID = 999L;
  private final Long COLUMN_ID = 2L;
  private final String BOARD_TITLE = "test board";
  private final String SPECIFIC_BOARD_URL = "/boards/" + BOARD_ID;
  private final String BOARDS_URL = "/boards";
  private final String TOKEN = "SOMETOKEN";
  private final User USER = new User("john.doe@codurance.com", "John Doe");

  @MockBean
  private UserFactory userFactory;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private LoginService loginService;

  private MockMvcWrapper mockMvcWrapper;

  @MockBean
  private ApplicationBoardService applicationBoardService;

  @BeforeEach
  void setUp() {
    mockMvcWrapper = new MockMvcWrapper(context);
  }

  @Test
  void returns_board_with_columns_and_cards() throws Exception {
    String cardText = "hello";
    Long cardId = 1L;
    Integer voterCount = 0;
    CardResponseObject cardResponse = new CardResponseObject(cardText, cardId, COLUMN_ID, false,
        voterCount, USER.username);
    List<CardResponseObject> cards = List.of(cardResponse);
    ColumnResponseObject columnResponse = new ColumnResponseObject(COLUMN_ID,
        ColumnType.START.getTitle(),
        cards);
    List<ColumnResponseObject> columns = List.of(columnResponse);

    when(userFactory.create(TOKEN)).thenReturn(USER);
    when(applicationBoardService.getBoard(USER, BOARD_ID))
        .thenReturn(new BoardResponseObject(BOARD_ID, BOARD_TITLE, columns));

    String jsonResponse = mockMvcWrapper
        .getRequest(SPECIFIC_BOARD_URL, status().isOk(), getAuthHeader(TOKEN));
    BoardResponseObject boardResponse = mockMvcWrapper
        .buildObject(jsonResponse, BoardResponseObject.class);

    ColumnResponseObject columnResponseObj = boardResponse.getColumns().get(0);
    assertEquals(cards.size(), columnResponseObj.getCards().size());
    CardResponseObject cardResponseObj = columnResponseObj.getCards().get(0);
    assertEquals(cardText, cardResponseObj.getText());
    assertEquals(cardId, cardResponseObj.getId());
    assertEquals(COLUMN_ID, cardResponseObj.getColumnId());
    assertEquals(USER.username, cardResponseObj.getAuthor());
  }

  @Test
  void returns_id_and_title_of_users_boards() throws Exception {
    when(userFactory.create(TOKEN)).thenReturn(USER);
    when(applicationBoardService.getUserBoards(USER))
        .thenReturn(List.of(new UserBoardResponseObject(BOARD_ID, BOARD_TITLE)));

    String jsonResponse = mockMvcWrapper
        .getRequest(BOARDS_URL, status().isOk(), getAuthHeader(TOKEN));
    List<Board> boards = objectMapper.readValue(jsonResponse, new TypeReference<>() {
    });

    assertEquals(1, boards.size());
    assertEquals(BOARD_TITLE, boards.get(0).getTitle());
    assertEquals(BOARD_ID, boards.get(0).getId());
  }

  @Test
  public void returns_bad_request_when_board_does_not_exist() throws Exception {
    when(userFactory.create(TOKEN)).thenReturn(USER);
    doThrow(new BoardNotFoundException()).when(applicationBoardService)
        .getBoard(USER, NON_EXISTENT_BOARD_ID);
    String jsonResponse = mockMvcWrapper
        .getRequest(BOARDS_URL + "/" + NON_EXISTENT_BOARD_ID, status().isBadRequest(),
            getAuthHeader(TOKEN));
    List<String> response = mockMvcWrapper.buildObject(jsonResponse);
    assertEquals("Board Id is not valid", response.get(0));
  }

  @Test
  void returns_a_new_board() throws Exception {
    when(loginService.isAuthorized(USER.email, TOKEN)).thenReturn(true);
    when(userFactory.create(TOKEN)).thenReturn(USER);
    when(applicationBoardService.createBoard(any(NewBoardRequestObject.class)))
        .thenReturn(new BoardResponseObject(BOARD_ID, BOARD_TITLE, Collections.emptyList()));

    NewBoardRequestObject requestObject = new NewBoardRequestObject(BOARD_TITLE, USER.email);
    String jsonResponse = mockMvcWrapper
        .postRequest(BOARDS_URL, asJsonString(requestObject), status().isCreated(),
            getAuthHeader(TOKEN));
    BoardResponseObject boardResponse = mockMvcWrapper
        .buildObject(jsonResponse, BoardResponseObject.class);

    assertEquals(BOARD_TITLE, boardResponse.getTitle());
    assertEquals(BOARD_ID, boardResponse.getId());
  }

  @Test
  void returns_bad_request_when_title_is_empty() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject("", "john.doe@codurance.com");
    String content = asJsonString(requestObject);
    String jsonResponse = mockMvcWrapper
        .postRequest(BOARDS_URL, content, status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);

    assertEquals("Title must not be less than 1 character", errorResponse.get(0));
  }

  @Test
  void returns_bad_request_when_title_is_null() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject(null, "john.doe@codurance.com");
    String jsonResponse = mockMvcWrapper
        .postRequest(BOARDS_URL, asJsonString(requestObject),
            status().isBadRequest(), new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);

    assertEquals("Title cannot be empty", errorResponse.get(0));
  }

  @Test
  void returns_bad_request_when_userEmail_is_invalid() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject("test board", "invalid email");
    String jsonResponse = mockMvcWrapper
        .postRequest(BOARDS_URL, asJsonString(requestObject), status().isBadRequest(),
            new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);

    assertEquals("Email is invalid", errorResponse.get(0));
  }

  @Test
  void returns_bad_request_when_email_is_null() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject("test board", null);
    String jsonResponse = mockMvcWrapper
        .postRequest(BOARDS_URL, asJsonString(requestObject), status().isBadRequest(),
            new HttpHeaders());
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);

    assertEquals("Email is required", errorResponse.get(0));
  }

  @Test
  void throws_UnauthorizedException_when_email_does_not_match_the_token() throws Exception {
    NewBoardRequestObject requestObject = new NewBoardRequestObject(BOARD_TITLE, USER.email);
    when(loginService.isAuthorized(USER.email, TOKEN)).thenThrow(new UnauthorizedException());
    String jsonResponse = mockMvcWrapper
        .postRequest(BOARDS_URL, asJsonString(requestObject), status().isUnauthorized(),
            getAuthHeader(TOKEN));
    List<String> errorResponse = mockMvcWrapper.buildObject(jsonResponse);

    assertEquals("Unauthorized", errorResponse.get(0));
  }

}
