Feature: create a board

  Scenario: client creates a board
    Given a user is logged in
    When the client sends the title of the board "test board" and their email
    Then the client receives the new board with title "test board"