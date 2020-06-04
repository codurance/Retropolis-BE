package com.codurance.retropolis.utils;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class MockMvcWrapper {

  private final MockMvc mockMvc;

  private final ObjectMapper objectMapper;

  public MockMvcWrapper(WebApplicationContext context) {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    objectMapper = new ObjectMapper();
  }

  public static HttpHeaders getAuthHeader(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, token);
    return headers;
  }

  public <T> T postRequest(String url, String content, ResultMatcher response) throws Exception {
    String responseBody = mockMvc.perform(MockMvcRequestBuilders.post(url)
        .content(content)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON))
        .andExpect(response)
        .andReturn().getResponse().getContentAsString();
    return buildObject(responseBody);
  }

  public <T> T postRequest(String url, String content, ResultMatcher response, Class<T> objectType) throws Exception {
    String responseBody = mockMvc.perform(MockMvcRequestBuilders.post(url)
        .content(content)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON))
        .andExpect(response)
        .andReturn().getResponse().getContentAsString();
    return buildObject(responseBody, objectType);
  }

  public <T> T getRequest(String url, ResultMatcher response, HttpHeaders headers) throws Exception {
    String responseBody = mockMvc.perform(MockMvcRequestBuilders.get(url)
        .headers(headers)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON))
        .andExpect(response).andReturn().getResponse().getContentAsString();
    return buildObject(responseBody);
  }

  public <T> T getRequest(String url, ResultMatcher response, HttpHeaders headers, Class<T> objectType) throws Exception {
    String responseBody = mockMvc.perform(MockMvcRequestBuilders.get(url)
        .headers(headers)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON))
        .andExpect(response).andReturn().getResponse().getContentAsString();
    return buildObject(responseBody, objectType);
  }

  public <T> T deleteRequest(String url, ResultMatcher response) throws Exception {
    String responseBody = mockMvc.perform(MockMvcRequestBuilders.delete(url)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(response).andReturn().getResponse().getContentAsString();

    return buildObject(responseBody);
  }

  public <T> T patchRequest(String url, String content, ResultMatcher response, Class<T> objectType) throws Exception {
    String responseBody = mockMvc.perform(MockMvcRequestBuilders.patch(url)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(response).andReturn().getResponse().getContentAsString();

    return buildObject(responseBody, objectType);
  }

  public <T> T patchRequest(String url, String content, ResultMatcher response) throws Exception {
    String responseBody = mockMvc.perform(MockMvcRequestBuilders.patch(url)
        .content(content)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(response).andReturn().getResponse().getContentAsString();

    return buildObject(responseBody);
  }

  private <T> T buildObject(String responseBody) throws JsonProcessingException {
    return objectMapper.readValue(responseBody, new TypeReference<>() {
    });
  }

  private <T> T buildObject(String responseBody, Class<T> objectType)
      throws JsonProcessingException {
    return objectMapper.readValue(responseBody, objectType);
  }

}

