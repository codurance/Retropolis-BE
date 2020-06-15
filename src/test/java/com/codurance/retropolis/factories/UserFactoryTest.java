package com.codurance.retropolis.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.web.config.GoogleTokenAuthenticator;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserFactoryTest {

  public static final String EMAIL = "john.doe@codurance.com";
  public static final String USER_NAME = "John Doe";
  private static final String TOKEN = "google_id123";
  @Mock
  private GoogleTokenAuthenticator tokenAuthenticator;


  @Test
  void creates_a_new_user_from_token() throws GeneralSecurityException, IOException {
    when(tokenAuthenticator.getEmail(TOKEN)).thenReturn(EMAIL);
    when(tokenAuthenticator.getUsername(TOKEN)).thenReturn(USER_NAME);

    UserFactory userFactory = new UserFactory(tokenAuthenticator);
    User user = userFactory.create(TOKEN);
    assertEquals(EMAIL, user.email);
    assertEquals(USER_NAME, user.username);
  }
}
