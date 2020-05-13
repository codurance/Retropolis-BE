package com.codurance.retropolis.controllers;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public List<Card> getCards() {
        return cardService.getCards();
    }

    @PostMapping
    public ResponseEntity postCard(@RequestBody String request) {
        cardService.addCard(request);
        return new ResponseEntity(null, HttpStatus.CREATED);
    }
}
