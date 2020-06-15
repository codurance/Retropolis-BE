Feature: creating a board

  Scenario: user creates a board
    Given a user is logged in
    When the user creates a board with title:"test board" and their email
    Then the user receives the new board with title "test board"