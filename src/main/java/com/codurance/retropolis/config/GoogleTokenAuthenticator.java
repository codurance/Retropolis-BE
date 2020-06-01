package com.codurance.retropolis.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Profile(Environment.PROD)
public class GoogleTokenAuthenticator implements HandlerInterceptor {

  private final String CLIENT_ID = "582070750046-gn9mvl54av9j8b3mo3ea807c18di9ees.apps.googleusercontent.com";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String token = request.getHeader("Authorization");
    try {
      authenticateToken(token, request);
    } catch (Exception exception) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }

    return true;
  }

  private void authenticateToken(String token, HttpServletRequest request)
      throws GeneralSecurityException, IOException {
    GoogleIdTokenVerifier verifier = buildGoogleIdTokenVerifier();
    GoogleIdToken idToken = verifier.verify(token);
    request.getSession().setAttribute("name", idToken.getPayload().get("name"));
  }

  private GoogleIdTokenVerifier buildGoogleIdTokenVerifier() {
    JacksonFactory jsonFactory = new JacksonFactory();
    HttpTransport transport = new NetHttpTransport();
    return new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(Collections.singletonList(CLIENT_ID))
        .build();
  }
}
