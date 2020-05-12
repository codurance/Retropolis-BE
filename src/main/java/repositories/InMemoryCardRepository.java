package repositories;

import models.Card;

import java.util.ArrayList;
import java.util.List;

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
