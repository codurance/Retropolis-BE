package com.codurance.retropolis.controllers;

import com.codurance.retropolis.services.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
    public void post_cards_should_return_back_card_instance_with_id_in_response() throws Exception {
//        int cardId = 1;
//        int columnId = 1;
//        String cardText = "hello";
//        String userName = "John Doe";
//        NewCardRequestObject requestObject = new NewCardRequestObject(cardText, columnId, userName);
//        given(cardService.addCard(any(NewCardRequestObject.class))).willReturn(new Card(cardId, cardText, columnId, userName));
//
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post(URL)
//                .content(asJsonString(requestObject))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated()).andReturn();
//
//        String responseBody = response.getResponse().getContentAsString();
//        Card cardResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
//        });
//
//        assertEquals(cardText, cardResponse.getText());
//        assertEquals(cardId, cardResponse.getId());
//        assertEquals(columnId, cardResponse.getColumnId());
//        assertEquals(userName, cardResponse.getUserName());
    }

}
