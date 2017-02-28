package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.potatonet.data.PostService;
import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

@Controller
@RequestMapping("/users/{userId}")
public class UserController {

  private final UserService userService;
  private final PostService postService;

  @Autowired
  public UserController(UserService userService, PostService postService) {
    this.userService = userService;
    this.postService = postService;
  }

  @ModelAttribute
  public User user(@PathVariable("userId") Long userId) {
    return userService.findById(userId);
  }

  @GetMapping
  public String doGet(@ModelAttribute User user, Model model) {
    model.addAttribute("user", user);
    model.addAttribute("posts", postService.getUserProfilePosts(user));
    return "profile";
  }
}
