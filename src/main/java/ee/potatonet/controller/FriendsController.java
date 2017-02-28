package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @PostMapping
  @ResponseBody
  public String doPost(@ModelAttribute User user, @RequestParam("friendRequestId") Long friendRequestId) {
    User friendUser = userService.findById(friendRequestId);
    userService.addFriendship(user, friendUser);
    return "";
  }
}
