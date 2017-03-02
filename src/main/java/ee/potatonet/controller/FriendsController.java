package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

@Controller
@RequestMapping("/users/{userId}/friends")
public class FriendsController {
  
  private final UserService userService;

  @Autowired
  public FriendsController(UserService userService) {
    this.userService = userService;
  }

  @ModelAttribute
  public User user(@PathVariable("userId") Long userId) {
    return userService.findById(userId);
  }

  @GetMapping
  public String doGet(@ModelAttribute User user, Model model) {
    model.addAttribute("user", user);
    return "friends";
  }

  @PutMapping("/{friendRequestId}")
  @ResponseBody
  public void doIdPut(@ModelAttribute User user, @PathVariable("friendRequestId") Long friendRequestId) {
    User friendUser = userService.findById(friendRequestId);
    userService.addFriendship(user, friendUser);
  }

  @DeleteMapping("/{friendId}")
  @ResponseBody
  public void doIdDelete(@ModelAttribute User user, @PathVariable("friendId") Long friendId) {
    User friend = userService.findById(friendId);
    userService.removeFriends(user, friend);
  }
}
