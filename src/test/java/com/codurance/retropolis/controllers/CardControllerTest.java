package com.codurance.retropolis.controllers;

import com.codurance.retropolis.models.Card;
import com.codurance.retropolis.requests.NewCardRequestObject;
import com.codurance.retropolis.services.CardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
public class CardControllerTest {
    private static final String URL = "/cards";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void get_cards_should_return_empty_list_if_no_cards() throws Exception {
        given(cardService.getCards()).willReturn(Collections.emptyList());

        MvcResult httpResponse = mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = httpResponse.getResponse().getContentAsString();
        List<Card> response = objectMapper.readValue(contentAsString, new TypeReference<List<Card>>() {
        });

        assertTrue(response.isEmpty());
    }

    @Test
    void get_cards_should_return_list_with_one_card_from_repo() throws Exception {
        String cardBody = "hello world!";
        given(cardService.getCards()).willReturn(List.of(new Card(cardBody, 1)));

        MvcResult httpResponse = mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk()).andReturn();
        String contentAsString = httpResponse.getResponse().getContentAsString();
        List<Card> response = objectMapper.readValue(contentAsString, new TypeReference<List<Card>>() {
        });

        assertEquals(1,response.size());
        assertEquals(cardBody, response.get(0).getBody());
        assertEquals(1, response.get(0).id);
    }

    @Test
    void post_cards_should_return_back_card_instance_with_id_in_response() throws Exception {
        String cardText = "hello";
        NewCardRequestObject requestObject = new NewCardRequestObject(cardText);
        given(cardService.addCard(any(NewCardRequestObject.class))).willReturn(new Card(cardText, 1));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(asJsonString(requestObject))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();


        String responseBody = response.getResponse().getContentAsString();
        Card cardResponse = objectMapper.readValue(responseBody, new TypeReference<Card>() {
        });

        assertEquals(cardText, cardResponse.getBody());
        assertEquals(1, cardResponse.id);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
