Feature: retrieving a board

  Scenario: user requests a specific board
    When the user requests the test board
    Then the user receives board with three columns, "Start", "Stop", and "Continue"
