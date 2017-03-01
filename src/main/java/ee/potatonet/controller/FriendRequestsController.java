package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

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
  @ResponseBody
  public void doPost(@ModelAttribute User user, @CurrentUser User currentUser) {
    userService.addFriendRequest(currentUser, user);
  }

  @DeleteMapping("/{friendRequestId}")
  @ResponseBody
  public void doIdDelete(@ModelAttribute User user, @PathVariable("friendRequestId") Long friendRequestId) {
    User friend = userService.findById(friendRequestId);
    userService.removeFriendRequests(user, friend);
  }
}
