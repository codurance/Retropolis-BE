Feature: updating a card

  Scenario: user updates a card
    Given the card exists
    When the user updates the existing card with text "bye"
    Then the user receives the card with the text:"bye"