Feature: card is deleted

  Scenario: client makes call to DELETE /cards/1
    Given the card exists
    And the client deletes to cards with this id passing it as path variable to endpoint
    Then the client receives a status code of 200 after card was deleted