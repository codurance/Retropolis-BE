Feature: deleting a card

  Scenario: user deletes a card
    Given the card exists
    When the user deletes existing card
    Then the user receives an ok response