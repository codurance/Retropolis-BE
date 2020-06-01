Feature: board is retrieved

  Scenario: client makes call to GET /boards
    When the client calls /boards/1
    Then the client receives status code of 200
    And the client receives board with three columns, "Start", "Stop", and "Continue"
