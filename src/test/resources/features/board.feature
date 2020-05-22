Feature: board is retrieved

  Scenario: client makes call to GET /board
    When the client calls /board
    Then the client receives status code of 200
    And the client receives board with three columns, "Start", "Stop", and "Continue"