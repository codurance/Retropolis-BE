Feature: card is created

  Scenario: client makes call to POST /cards
    When the client posts to cards endpoint with column_id:1 and text:"hello"
    Then the client receives status code of 201
    And the client receives the card with the column_id:1 and text:"hello"