package com.codurance.retropolis.web.responses;

import static java.util.stream.Collectors.toList;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.services.UserService;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardResponseObjectFactory {

  private final UserService userService;

  @Autowired
  public BoardResponseObjectFactory(UserService userService) {
    this.userService = userService;
  }

  public BoardResponseObject create(Board board, Long userId) {
    List<ColumnResponseObject> columns = board.getColumns().stream().map(convertToColumnResponseObject(userId)).collect(toList());
    return new BoardResponseObject(board.getId(), board.getTitle(), columns);
  }

  private Function<Column, ColumnResponseObject> convertToColumnResponseObject(Long userId) {
    return col -> {
      List<CardResponseObject> cards = col.getCards().stream().map(convertToCardResponseObject(userId))
          .collect(toList()); // TODO use join to get usernames from cards
      return new ColumnResponseObject(col.getId(), col.getTitle(), cards);
    };
  }

  private Function<Card, CardResponseObject> convertToCardResponseObject(Long userId) {
    return card -> new CardResponseObject(card.getText(), card.getId(), card.getColumnId(),
        card.getVoters().contains(userId),
        card.getVoters().size(), userService.findById(card.getUserId()).username);
  }

  public List<UserBoardResponseObject> create(List<Board> boards) {
    return boards.stream()
        .map(board -> new UserBoardResponseObject(board.getId(), board.getTitle()))
        .collect(toList());
  }
}
