Feature: upvote is added to card

  Scenario: client makes call to PATCH /cards/1/vote
    Given the card exists
    When the client adds card vote with voter:"john.doe@codurance.com"
    Then the client receives the card with their vote