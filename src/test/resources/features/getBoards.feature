Feature: all users boards are retrieved

  Scenario: user has visited a board
    Given a user has accessed the test board
    When the user requests all their boards
    Then the user receives a list of the boards with one called "test board"

  Scenario: user has not visited a board
    When the user requests all their boards
    Then the user receives a empty list of boards