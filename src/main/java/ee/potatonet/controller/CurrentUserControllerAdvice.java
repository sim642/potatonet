package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ee.potatonet.data.model.User;
import ee.potatonet.data.service.UserService;

@ControllerAdvice
public class CurrentUserControllerAdvice {
  
  private final UserService userService;

  @Autowired
  public CurrentUserControllerAdvice(UserService userService) {
    this.userService = userService;
  }

  @ModelAttribute
  public void currentUserAttribute(@CurrentUser User currentUser, Model model) {
    if (currentUser != null) {
      currentUser = userService.find(currentUser);
      model.addAttribute("currentUser", currentUser);
      model.addAttribute("incomingFriendRequestCount", userService.countUserIncomingFriendRequests(currentUser));
    }
  }
}
