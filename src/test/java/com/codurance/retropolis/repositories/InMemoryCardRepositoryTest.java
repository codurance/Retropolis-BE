package com.codurance.retropolis.repositories;

import com.codurance.retropolis.models.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryCardRepositoryTest {

    private CardRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryCardRepository();
    }

    @Test
    void should_return_empty_list_when_no_cards() {
        List<Card> cards = repo.getAll();

        assertTrue(cards.isEmpty());
    }

//    @Test
//    void should_return_card_after_saving() {
//        Card card = new Card("new card");
//        repo.save(card);
//
//        List<Card> cards = repo.getAll();
//
//        assertEquals(1, cards.size());
//        assertEquals(card, cards.get(0));
//    }
}