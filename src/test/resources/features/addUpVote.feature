Feature: up vote is added to card

  Scenario: client makes call to PATCH /cards/1 with addUpVote
    When the card exists with id
    And the client updates to cards with this id in path and addUpVote "username" in body
    Then the client receives a status code of 200 after update
    And the client receives the card with the voter:"username"