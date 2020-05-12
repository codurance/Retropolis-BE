import models.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.CardRepository;
import repositories.InMemoryCardRepository;
import services.CardService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardAcceptanceTest {

    private CardRepository cardRepository;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardRepository = new InMemoryCardRepository();
        cardService = new CardService(cardRepository);
    }

    @Test
    void should_return_empty_list_when_no_cards() {
        List<Card> cards = cardService.getCards();
        assertTrue(cards.isEmpty());
    }

    @Test
    void should_return_list_with_card_after_adding() {
        String body = "new card";
        cardService.addCard(body);
        List<Card> cards = cardService.getCards();

        assertEquals(1, cards.size());
        assertEquals(body, cards.get(0).getBody());
    }
}
