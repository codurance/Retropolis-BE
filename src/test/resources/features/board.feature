Feature: board is retrieved

  Scenario: client makes call to GET /boards
    When the client calls /boards/1
    Then the client receives status code of 200
    And the client receives board with three columns, "Start", "Stop", and "Continue"

Feature: all users boards are retrieved
  Scenario: user requests all boards
    Given a user has previously accessed a board called "first sprint"
    When the user requests all their boards
    Then the user receives a list of the boards with one called "first sprint"