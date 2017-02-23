package ee.potatonet.controller;


import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.potatonet.data.Post;
import ee.potatonet.data.PostService;
import ee.potatonet.data.User;

@Controller
public class FeedController {

  private final PostService postService;

  @Autowired
  public FeedController(PostService postService) {
    this.postService = postService;
  }

  @RequestMapping(value = "/feed", method = RequestMethod.GET)
  public String doGet(@CurrentUser User currentUser, Model model) {
    model.addAttribute("post", new Post());
    model.addAttribute("posts", postService.getUserFeedPosts(currentUser));
    return "feed";
  }
  
  @RequestMapping(value = "/feed", method = RequestMethod.POST)
  public String savePost(@CurrentUser User currentUser, @ModelAttribute Post post) {
    postService.savePostToUser(post, currentUser);
    
    return "redirect:/feed";
  }
  
  
}