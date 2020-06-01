package com.codurance.retropolis.services;

import com.codurance.retropolis.exceptions.UserNotFoundException;
import com.codurance.retropolis.models.User;
import com.codurance.retropolis.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private UserRepository userRepository;
  private BoardService boardService;

  public UserService(UserRepository userRepository,
      BoardService boardService) {
    this.userRepository = userRepository;
    this.boardService = boardService;
  }

  public void registerUserIfNotExists(String email, Long boardId) {
    User user = findOrCreateBy(email);
    boardService.addToBoard(user.id, boardId);
  }

  private User findOrCreateBy(String email) {
    try {
      return userRepository.findByEmail(email);
    } catch (UserNotFoundException userNotFoundException) {
      return userRepository.register(email);
    }
  }
}
