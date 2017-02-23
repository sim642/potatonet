package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ee.potatonet.data.User;
import ee.potatonet.data.repos.UserRepository;

@ControllerAdvice
public class CurrentUserControllerAdvice {
  
  private final UserRepository userRepository;

  @Autowired
  public CurrentUserControllerAdvice(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @ModelAttribute
  public void currentUserAttribute(@CurrentUser User currentUser, Model model) {
    if (currentUser != null) {
      currentUser = userRepository.findOne(currentUser.getId());
      model.addAttribute("currentUser", currentUser);
      model.addAttribute("incomingFriendRequestCount", userRepository.countIncomingFriendRequests(currentUser));
    }
  }
}
