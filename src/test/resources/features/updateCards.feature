Feature: updating card

  Scenario: client updates a card
    Given the card exists
    When the client updates the existing card with text "bye"
    Then the client receives the card with the text:"bye"