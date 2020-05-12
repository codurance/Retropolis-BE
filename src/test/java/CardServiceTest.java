import models.Card;
import org.junit.jupiter.api.Test;
import repositories.InMemoryCardRepository;
import services.CardService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardServiceTest {
    @Test
    void should_return_empty_list_of_cards() {
        CardService cardService = new CardService(new InMemoryCardRepository());
        List<Card> allCards = cardService.getCards();
        assertTrue(allCards.isEmpty());
    }
}
