package ee.potatonet.controller;


import java.time.ZonedDateTime;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.TemplateEngine;

import ee.potatonet.TemplateRenderService;
import ee.potatonet.data.Post;
import ee.potatonet.data.User;
import ee.potatonet.data.repos.PostRepository;

@Controller
public class FeedController {

  private final PostRepository postRepository;

  private final SimpMessagingTemplate simpMessagingTemplate;

  private final TemplateRenderService templateRenderService;

  @Autowired
  public FeedController(PostRepository postRepository, SimpMessagingTemplate simpMessagingTemplate, TemplateEngine templateEngine, TemplateRenderService templateRenderService) {
    this.postRepository = postRepository;
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.templateRenderService = templateRenderService;
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

  @MessageMapping("/fromSockJSClient")
  public void handlePost(@CurrentUser User currentUser) {
    simpMessagingTemplate.convertAndSend("/feed/websocket", "{\"content\": \"stuff\"}");

    Post post = new Post(currentUser, "test");
    String output = templateRenderService.render("common", Collections.singleton("post"), Collections.singletonMap("postInfo", post));
    System.out.println(output);
  }
  
}