Feature: card is created

  Scenario: client makes call to PATCH /cards/1
    When the card exists with an id
    And the client updates to cards with this id and changes the text to "bye"
    Then the client receives 200 status code
    And the client receives the card with the text:"bye"