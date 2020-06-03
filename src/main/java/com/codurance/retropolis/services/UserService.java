package com.codurance.retropolis.services;

import com.codurance.retropolis.entities.User;
import com.codurance.retropolis.exceptions.UserNotFoundException;
import com.codurance.retropolis.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final BoardService boardService;

  public UserService(UserRepository userRepository,
      BoardService boardService) {
    this.userRepository = userRepository;
    this.boardService = boardService;
  }

  public void registerUserIfNotExists(String email, Long boardId) {
    User user = findOrCreateBy(email);
    boardService.addToBoard(user.id, boardId);
  }

  public User findOrCreateBy(String email) {
    try {
      return userRepository.findByEmail(email);
    } catch (UserNotFoundException userNotFoundException) {
      return userRepository.register(email);
    }
  }
}
