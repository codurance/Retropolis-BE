package com.codurance.retropolis.services;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.repositories.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Captor
    private ArgumentCaptor<Card> captor;
    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardService(cardRepository);
    }

    @Test
    void should_return_empty_list_of_cards() {
        when(cardRepository.getAll()).thenReturn(Collections.emptyList());

        List<Card> allCards = cardService.getCards();
        verify(cardRepository).getAll();

        assertTrue(allCards.isEmpty());
    }

    @Test
    void should_return_list_with_one_element_if_one_card_in_repo() {
        String cardText = "hello";
        int cardId = 1;
        when(cardRepository.getAll()).thenReturn(List.of(new Card(cardText, cardId)));

        List<Card> cards = cardService.getCards();
        assertEquals(1, cards.size());
        assertEquals(cardText, cards.get(0).getBody());
        assertEquals(cardId, cards.get(0).id);
    }

//    @Test
//    void should_add_a_card_to_repo() {
//        String body = "new card";
//        cardService.addCard(body);
//        verify(cardRepository).save(captor.capture());
//        Card card = captor.getValue();
//        assertEquals(body, card.getBody());
//    }
}
