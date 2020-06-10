Feature: board is retrieved

  Scenario: client requests a specific board
    When the client requests the test board
    Then the client receives board with three columns, "Start", "Stop", and "Continue"
