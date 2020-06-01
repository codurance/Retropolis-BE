package com.codurance.retropolis.services;

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

  public void registerUserIfNotExists(String email, Long boardId){
    User user = findByEmail(email);
    boardService.addToBoard(user.id, boardId);
//    try {
//      User user = findByEmail(email);
//    } catch (UserNotFoundException userNotFoundException){
//      User createdUser = userRepository.registerBy(email);
//    } finally {
//      boardService.addToBoard(createdUser.id, boardId);
//    }
  }

  private User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

}
