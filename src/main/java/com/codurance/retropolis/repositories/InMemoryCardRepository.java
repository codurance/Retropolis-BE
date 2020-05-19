package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Card;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCardRepository implements CardRepository {

    private final List<Card> cards;

    public InMemoryCardRepository() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getAll() {
        return cards;
    }
}
