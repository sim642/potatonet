package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.potatonet.controller.advice.CurrentUser;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.UserService;

@Controller
@RequestMapping("/users/{userId}/friendrequests")
public class FriendRequestsController {

  private final UserService userService;

  @Autowired
  public FriendRequestsController(UserService userService) {
    this.userService = userService;
  }

  @ModelAttribute
  public User user(@PathVariable("userId") Long userId) {
    return userService.findById(userId);
  }

  @PostMapping
  public String doPost(@ModelAttribute User user, @CurrentUser User currentUser, Model model) {
    userService.addFriendRequest(currentUser, user);

    model.addAttribute("user", user);
    return "common :: friendButtons";
  }

  @DeleteMapping("/{friendRequestId}")
  public String doIdDelete(@ModelAttribute User user, @PathVariable("friendRequestId") Long friendRequestId, Model model) {
    User friendRequest = userService.findById(friendRequestId);
    userService.removeFriendRequests(user, friendRequest);

    model.addAttribute("user", friendRequest);
    return "common :: friendButtons";
  }
}
