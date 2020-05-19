package com.codurance.retropolis.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
