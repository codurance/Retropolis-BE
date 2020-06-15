Feature: card is created

  Scenario: client creates a card
    When the client posts to cards endpoint with column_id:1, text:"hello" and email:"john.doe@codurance.com"
    Then the client receives the card with the column_id:1, text:"hello" and userId:1