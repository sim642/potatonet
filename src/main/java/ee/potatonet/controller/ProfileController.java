package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.potatonet.data.PostService;
import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

@Controller
public class ProfileController {

  private final UserService userService;
  private final PostService postService;

  @Autowired
  public ProfileController(UserService userService, PostService postService) {
    this.userService = userService;
    this.postService = postService;
  }

  @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
  public String doGet(@PathVariable("id") long id, Model model) {
    User user = userService.findById(id);
    model.addAttribute("user", user);
    model.addAttribute("posts", postService.getUserProfilePosts(user));

    return "profile";
  }

  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  public String doGet(@CurrentUser User currentUser, Model model) {
    User user = userService.find(currentUser);
    model.addAttribute("user", user);
    model.addAttribute("posts", postService.getUserProfilePosts(user));

    return "profile";
  }

  @PostMapping(value = "/profile/add/{id}")
  public String doGet(@CurrentUser User currentUser, @PathVariable("id") long id, Model model) {
    userService.addFriendRequest(currentUser, userService.findById(id));

    return "redirect:/profile/" + id;
  }
}
