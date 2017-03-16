package ee.potatonet.controller;


import javax.validation.Valid;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ee.potatonet.data.model.Post;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.PostService;
import ee.potatonet.data.service.UserService;

@Controller
public class FeedController {

  private final PostService postService;
  private final UserService userService;

  @Autowired
  public FeedController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String doGet(@CurrentUser User currentUser, @RequestParam(value = "beforePostId", required = false) Long beforePostId, Model model) {
    model.addAttribute("post", new Post(userService.find(currentUser), ""));
    if (beforePostId != null) {
      model.addAttribute("posts", postService.getUserFeedPosts(currentUser, postService.findById(beforePostId)));
      model.addAttribute("userIds", Collections.emptyList());
    }
    else {
      model.addAttribute("posts", postService.getUserFeedPosts(currentUser, null));
      model.addAttribute("userIds", userService.getUserFeedUserIds(currentUser));
    }
    return "feed";
  }

  @PostMapping(value = "/", headers = "X-Requested-With!=XMLHttpRequest")
  public String doPost(@CurrentUser User currentUser, @Valid @ModelAttribute Post post) {
    postService.savePostToUser(post, currentUser);
    return "redirect:/";
  }

  @PostMapping(value = "/", headers = "X-Requested-With=XMLHttpRequest")
  @ResponseBody
  public void doPostAjax(@CurrentUser User currentUser, @Valid @ModelAttribute Post post) {
    postService.savePostToUser(post, currentUser);
  }

  @GetMapping("/posts")
  public String doGetPosts(@CurrentUser User currentUser, @RequestParam("beforePostId") Long beforePostId, Model model) {
    model.addAttribute("posts", postService.getUserFeedPosts(currentUser, postService.findById(beforePostId)));
    return "posts";
  }
}