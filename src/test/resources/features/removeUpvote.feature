Feature: removing upvote from a card

  Scenario: user removes their upvote from a card
    Given the card exists
    And the user adds card vote with voter:"john.doe@codurance.com"
    When the user removes card vote with voter:"john.doe@codurance.com"
    Then the user receives the card without their vote