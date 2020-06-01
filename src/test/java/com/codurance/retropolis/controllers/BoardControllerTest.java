package com.codurance.retropolis.controllers;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.codurance.retropolis.models.Board;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.models.Column;
import com.codurance.retropolis.services.BoardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
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
  private static final String BOARD_URL = "/boards/" + BOARD_ID;
  private static final String USERS_BOARDS = "/boards";

  @MockBean
  private BoardService boardService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

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

  @Test
  void returns_id_and_title_of_users_boards() throws Exception {
    Long userId = 1L;
    when(boardService.getUsersBoards(userId)).thenReturn(List.of(new Board(BOARD_ID, BOARD_TITLE, emptyList())));

    List<Board> boards = requestUsersBoards();

    assertEquals(1, boards.size());
    assertEquals(BOARD_TITLE, boards.get(0).getTitle());
    assertEquals(BOARD_ID, boards.get(0).getId());
  }

  private Board requestBoard() throws Exception {
    MvcResult httpResponse = mockMvc.perform(MockMvcRequestBuilders.get(BOARD_URL))
        .andExpect(status().isOk()).andReturn();
    String contentAsString = httpResponse.getResponse().getContentAsString();
    return objectMapper.readValue(contentAsString, new TypeReference<>() {
    });
  }

  private List<Board> requestUsersBoards() throws Exception {
    MvcResult httpResponse = mockMvc.perform(MockMvcRequestBuilders.get(USERS_BOARDS))
        .andExpect(status().isOk()).andReturn();
    String contentAsString = httpResponse.getResponse().getContentAsString();
    return objectMapper.readValue(contentAsString, new TypeReference<>() {
    });
  }
}
