package com.codurance.retropolis.config.web;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface TokenAuthenticator {

  String getEmail(String token) throws GeneralSecurityException, IOException;

  String getUsername(String token) throws GeneralSecurityException, IOException;
}
