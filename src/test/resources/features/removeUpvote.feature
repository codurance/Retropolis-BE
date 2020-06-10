Feature: removing upvote from a card

  Scenario: client removes their upvote from a card
    Given the card exists
    And the client adds card vote with voter:"john.doe@codurance.com"
    When the client removes card vote with voter:"john.doe@codurance.com"
    Then the client receives the card without their vote