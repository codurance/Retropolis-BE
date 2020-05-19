package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.factories.CardFactory;
import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.repositories.CardRepository;
import com.codurance.retropolis.requests.NewCardRequestObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock private CardRepository cardRepository;
    @Mock private CardFactory cardFactory;

    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardService(cardRepository, cardFactory);
    }

    @Test
    void should_add_and_return_new_card() {
        String text = "new card";
        NewCardRequestObject requestObject = new NewCardRequestObject(text);
        Card card = new Card(text, 1);

        when(cardFactory.create(requestObject)).thenReturn(card);
        when(cardRepository.save(card)).thenReturn(card);

        assertEquals(card, cardService.addCard(requestObject));
    }
}
