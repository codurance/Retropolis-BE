package com.codurance.retropolis.services;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getCards() {
        return cardRepository.getAll();
    }

    public void addCard(String body) {
        cardRepository.save(new Card(body));
    }
}
