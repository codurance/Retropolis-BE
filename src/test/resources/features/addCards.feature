Feature: card is created

  Scenario: client makes call to POST /cards
    When the client posts to cards endpoint with column_id:1, text:"hello" and email:"john.doe@codurance.com"
    Then the client receives a status code of 201
    And the client receives the card with the column_id:1, text:"hello" and userId:1