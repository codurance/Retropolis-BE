Feature: card is deleted

  Scenario: client deletes a card
    Given the card exists
    When the client deletes existing card
    Then the client receives an ok response