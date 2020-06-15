Feature: upvote is added to card

  Scenario: client adds vote to a card
    Given the card exists
    When the client adds card vote with voter:"john.doe@codurance.com"
    Then the client receives the card with their vote