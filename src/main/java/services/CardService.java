package services;

import models.Card;
import org.springframework.stereotype.Service;
import repositories.CardRepository;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

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
