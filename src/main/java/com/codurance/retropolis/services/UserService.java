package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.UserNotFoundException;
import com.codurance.retropolis.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void registerUserIfNotExists(User requestUser, Long boardId) {
    User user = findOrCreateBy(requestUser);
    addToBoard(user.getId(), boardId);
  }

  public void addToBoard(Long userId, Long boardId) {
    userRepository.addToBoard(userId, boardId);
  }

  public User findOrCreateBy(User user) {
    try {
      return userRepository.findByEmail(user.email);
    } catch (UserNotFoundException userNotFoundException) {
      return userRepository.register(user);
    }
  }
}
