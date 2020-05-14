package com.codurance.retropolis.services;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.repositories.CardRepository;
import com.codurance.retropolis.requests.NewCardRequestObject;
import org.springframework.stereotype.Component;

@Component
public class CardFactory {

    private CardRepository cardRepository;

    public CardFactory(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card create(NewCardRequestObject requestObject) {
        int id = cardRepository.getAll().size() + 1;
        return new Card(requestObject.getText(), id);
    }
}
