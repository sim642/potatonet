package ee.potatonet.controller;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ee.potatonet.controller.advice.CurrentUser;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.PostService;
import ee.potatonet.data.service.UserService;

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
  public String doGet(@ModelAttribute User user, @RequestParam(value = "beforePostId", required = false) Long beforePostId, Model model) {
    model.addAttribute("user", user);
    if (beforePostId != null) {
      model.addAttribute("posts", postService.getUserProfilePosts(user, postService.findById(beforePostId)));
      model.addAttribute("userIds", Collections.emptyList());
    }
    else {
      model.addAttribute("posts", postService.getUserProfilePosts(user, null));
      model.addAttribute("userIds", Collections.singletonList(user.getId()));
    }
    return "user";
  }

  @GetMapping("/posts")
  public String doGetPosts(@CurrentUser User currentUser, @RequestParam("beforePostId") Long beforePostId, Model model) {
    model.addAttribute("posts", postService.getUserProfilePosts(currentUser, postService.findById(beforePostId)));
    return "posts";
  }
}
