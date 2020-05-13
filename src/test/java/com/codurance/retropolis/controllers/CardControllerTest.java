package com.codurance.retropolis.controllers;

import com.codurance.retropolis.controllers.CardController;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.codurance.retropolis.models.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.codurance.retropolis.services.CardService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
public class CardControllerTest {

    private static final String DOMAIN_BASE_URL = "/cards";
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void get_cards_should_return_empty_list_if_no_cards() throws Exception {
        given(cardService.getCards()).willReturn(Collections.emptyList());

        MvcResult httpResponse = mockMvc.perform(MockMvcRequestBuilders.get(DOMAIN_BASE_URL))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = httpResponse.getResponse().getContentAsString();
        List<Card> response = objectMapper.readValue(contentAsString, new TypeReference<List<Card>>() {
        });

        assertTrue(response.isEmpty());
    }
}
