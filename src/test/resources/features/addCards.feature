Feature: card is created

  Scenario: client creates a card
    When the client posts to cards endpoint with text:"hello" and email:"john.doe@codurance.com"
    Then the client receives the card with the text:"hello" and author:"john doe"