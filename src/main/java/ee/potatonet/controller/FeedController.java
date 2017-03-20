package ee.potatonet.controller;


import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ee.potatonet.data.Post;
import ee.potatonet.data.PostService;
import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

import javax.validation.Valid;

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

  @PutMapping(value = "/like/{postId}", headers = "X-Requested-With=XMLHttpRequest")
  public String doLikeAjax(@CurrentUser User currentUser, @PathVariable("postId") long postId, Model model) {
    postService.toggleLike(currentUser, postId);
    model.addAttribute("post", postService.findById(postId));

    return "common :: likeButton";
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