package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

@Controller
public class FriendsController {
  
  private final UserService userService;

  @Autowired
  public FriendsController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(value = "/friends")
  public String doGet(Model model) {
    return "friends";
  }
  
  @PostMapping(value = "/friends/accept/{id}")
  public String acceptFriendRequest(@PathVariable("id") long id, @CurrentUser User currentUser) {
    userService.addFriendship(currentUser, userService.findById(id));
    
    return "redirect:/friends";
  }

  @PostMapping(value = "/friends/reject/{id}")
  public String rejectFriendRequest(@PathVariable("id") long id, @CurrentUser User currentUser) {
    userService.removeFriendRequests(currentUser, userService.findById(id));

    return "redirect:/friends";
  }

}
