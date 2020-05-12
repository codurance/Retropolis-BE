package services;

import models.Card;
import org.junit.jupiter.api.Test;
import repositories.CardRepository;
import repositories.InMemoryCardRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardServiceTest {
    @Test
    void should_return_empty_list_of_cards() {
        CardRepository cardRepository = new InMemoryCardRepository();
        CardService cardService = new CardService(cardRepository);

        List<Card> allCards = cardService.getCards();

        assertTrue(allCards.isEmpty());
    }
}
