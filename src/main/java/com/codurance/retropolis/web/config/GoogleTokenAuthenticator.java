package com.codurance.retropolis.web.config;

import com.codurance.retropolis.config.Environment;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Profile({Environment.PROD, Environment.DEV})
public class GoogleTokenAuthenticator implements HandlerInterceptor, TokenAuthenticator {

  @Value("${googleTokenId}")
  private String googleClientId;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    String token = request.getHeader("Authorization");
    try {
      authenticateToken(token, request);
    } catch (Exception exception) {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return false;
    }

    return true;
  }

  @Override
  public String getEmail(String token) throws GeneralSecurityException, IOException {
    GoogleIdTokenVerifier verifier = buildGoogleIdTokenVerifier();
    GoogleIdToken idToken = verifier.verify(token);
    return idToken.getPayload().get("email").toString();
  }

  @Override
  public String getUsername(String token) throws GeneralSecurityException, IOException {
    GoogleIdTokenVerifier verifier = buildGoogleIdTokenVerifier();
    GoogleIdToken idToken = verifier.verify(token);
    return idToken.getPayload().get("name").toString();
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
        .setAudience(Collections.singletonList(googleClientId))
        .build();
  }
}
