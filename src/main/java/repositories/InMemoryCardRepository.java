package repositories;

import models.Card;

import java.util.List;

public class InMemoryCardRepository implements CardRepository {
    @Override
    public List<Card> getAllCards() {
        throw new UnsupportedOperationException();
    }
}
