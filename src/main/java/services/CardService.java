package services;

import models.Card;
import repositories.CardRepository;

import java.util.List;

public class CardService {
    private CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getCards() {
        throw new UnsupportedOperationException("implement me!");
    }
}
