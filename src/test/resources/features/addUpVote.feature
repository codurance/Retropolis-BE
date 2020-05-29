Feature: upvote is added to card

  Scenario: client makes call to PATCH /cards/1/vote
    When the card exists with id
    And the client updates cards vote with this id in path and voter:"username" and addVote:"true" in body
    Then the client receives a status code of 200 after update
    And the client receives the card with the voter:"username"