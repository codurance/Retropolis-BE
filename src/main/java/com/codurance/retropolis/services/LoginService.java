package com.codurance.retropolis.services;

import com.codurance.retropolis.exceptions.UnauthorizedException;
import com.codurance.retropolis.web.config.TokenAuthenticator;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  private final TokenAuthenticator tokenAuthenticator;

  public LoginService(TokenAuthenticator tokenAuthenticator) {
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
