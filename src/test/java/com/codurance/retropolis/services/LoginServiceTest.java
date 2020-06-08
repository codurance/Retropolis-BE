package com.codurance.retropolis.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.config.web.GoogleTokenAuthenticator;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

  private static final String TOKEN = "TOKEN";
  private static final String USER_EMAIL = "john.doe@codurance.com";
  private static final String OTHER_USER_EMAIL = "jenny.doe@codurance.com";

  @Mock
  private GoogleTokenAuthenticator tokenAuthenticator;

  private LoginService loginService;

  @BeforeEach
  void setUp() {
    loginService = new LoginService(tokenAuthenticator);
  }

  @Test
  public void returns_true_when_email_does_not_match_the_token() throws GeneralSecurityException, IOException {
    when(tokenAuthenticator.getEmail(TOKEN)).thenReturn(USER_EMAIL);
    assertTrue(loginService.isAuthorized(USER_EMAIL, TOKEN));
  }

  @Test
  public void returns_false_when_email_does_not_match_the_token() throws GeneralSecurityException, IOException {
    when(tokenAuthenticator.getEmail(TOKEN)).thenReturn(USER_EMAIL);
    assertFalse(loginService.isAuthorized(OTHER_USER_EMAIL, TOKEN));
  }

}