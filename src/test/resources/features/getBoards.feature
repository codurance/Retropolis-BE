Feature: all users boards are retrieved
  Scenario: user requests all boards
    Given a board exists called "first sprint"
    Given a user has previously accessed "first sprint"
    When the user requests all their boards
    Then the user receives a list of the boards with one called "first sprint"