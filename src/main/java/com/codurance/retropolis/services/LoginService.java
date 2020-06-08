package com.codurance.retropolis.services;

import com.codurance.retropolis.config.web.GoogleTokenAuthenticator;
import com.codurance.retropolis.exceptions.UnauthorizedException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  private GoogleTokenAuthenticator tokenAuthenticator;

  public LoginService(GoogleTokenAuthenticator tokenAuthenticator) {
    this.tokenAuthenticator = tokenAuthenticator;
  }

  public Boolean isAuthorized(String email, String token) {
    try {
      return tokenAuthenticator.getEmail(token).equals(email);
    } catch (GeneralSecurityException | IOException e) {
      throw new UnauthorizedException();
    }
  }
}
