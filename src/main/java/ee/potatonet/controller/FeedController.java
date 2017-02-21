package ee.potatonet.controller;


import java.time.ZonedDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.potatonet.data.Post;
import ee.potatonet.data.User;
import ee.potatonet.data.repos.PostRepository;

@Controller
public class FeedController {

  private final PostRepository postRepository;

  @Autowired
  public FeedController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @RequestMapping(value = "/feed", method = RequestMethod.GET)
  public String doGet(@CurrentUser User currentUser, Model model) {
    model.addAttribute("post", new Post());
    model.addAttribute("posts", postRepository.findAllPostsByFriendsAndMe(currentUser));
    return "feed";
  }
  
  @RequestMapping(value = "/feed", method = RequestMethod.POST)
  public String savePost(@CurrentUser User currentUser, @ModelAttribute Post post) {
    post.setUser(currentUser);
    post.setCreationDateTime(ZonedDateTime.now());
    Post saved = postRepository.save(post);
    currentUser.getPosts().add(post);
    System.out.println(saved);
    postRepository.findAll();
    
    return "redirect:/feed";
  }
  
  
}