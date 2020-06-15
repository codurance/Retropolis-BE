Feature: adding an upvote

  Scenario: user adds an upvote to a card
    Given the card exists
    When the user adds card vote with voter:"john.doe@codurance.com"
    Then the user receives the card with their vote