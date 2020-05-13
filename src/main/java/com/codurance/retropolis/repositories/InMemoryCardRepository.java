package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Card;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryCardRepository implements CardRepository {
    private List<Card> cards;

    public InMemoryCardRepository() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getAll() {
        return cards;
    }

    public void save(Card card) {
        cards.add(card);
    }
}
