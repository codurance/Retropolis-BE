Feature: creating a card

  Scenario: user creates a card
    When the user posts to cards endpoint with text:"hello" and email:"john.doe@codurance.com"
    Then the user receives the card with the text:"hello" and author:"john doe"