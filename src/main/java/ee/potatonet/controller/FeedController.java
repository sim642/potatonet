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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ee.potatonet.controller.advice.CurrentUser;
import ee.potatonet.data.model.Post;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.PostService;
import ee.potatonet.data.service.UserService;
import ee.potatonet.web.MappingUtils;

@Controller
@RequestMapping("/")
public class FeedController {

  private final PostService postService;
  private final UserService userService;

  @Autowired
  public FeedController(PostService postService, UserService userService) {
    this.postService = postService;
    this.userService = userService;
  }

  @GetMapping
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

  @PostMapping(headers = MappingUtils.NON_AJAX_HEADERS)
  public String doPost(@CurrentUser User currentUser, @Valid @ModelAttribute Post post) {
    postService.savePostToUser(post, currentUser);
    return "redirect:/";
  }

  @PostMapping(headers = MappingUtils.AJAX_HEADERS)
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