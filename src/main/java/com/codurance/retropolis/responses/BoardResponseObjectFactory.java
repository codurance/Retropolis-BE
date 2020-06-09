package com.codurance.retropolis.responses;

import com.codurance.retropolis.entities.Board;
import com.codurance.retropolis.entities.Card;
import com.codurance.retropolis.entities.Column;
import com.codurance.retropolis.services.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardResponseObjectFactory {

  private UserService userService;

  @Autowired
  public BoardResponseObjectFactory(UserService userService) {
    this.userService = userService;
  }

  public BoardResponseObject create(Board board, Long userId) {
    // TODO map these with stream
    List<ColumnResponseObject> columns = new ArrayList();
    for (Column column : board.getColumns()) {
      List<CardResponseObject> cards = new ArrayList<>();
      for (Card card : column.getCards()) {
        // TODO use join to get usernames from cards
        cards.add(new CardResponseObject(card.getText(), card.getId(), card.getColumnId(),
            card.getVoters().contains(userId),
            card.getVoters().size(), userService.findById(card.getUserId()).username));
      }
      columns.add(new ColumnResponseObject(column.getId(), column.getTitle(), cards));
    }
    return new BoardResponseObject(board.getId(), board.getTitle(), columns);
  }
}
