package ee.potatonet.controller;


import java.time.ZonedDateTime;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
  public String doPost(@CurrentUser User currentUser, @ModelAttribute Post post) {
    createPost(currentUser, post);
    return "redirect:/feed";
  }

  @PostMapping("/post")
  @ResponseBody
  public String doPostAjax(@CurrentUser User currentUser, @ModelAttribute Post post) {
    createPost(currentUser, post);
    return "";
  }

  private void createPost(User currentUser, Post post) {
    post.setUser(currentUser);
    post.setCreationDateTime(ZonedDateTime.now());
    Post saved = postRepository.save(post);
    currentUser.getPosts().add(post);
    System.out.println(saved);
    postRepository.findAll();

    String output = templateRenderService.render("common", Collections.singleton("post"), Collections.singletonMap("postInfo", saved));
    System.out.println(output);

    simpMessagingTemplate.convertAndSendToUser(currentUser.getUsername(), "/feed", output);
    currentUser.getFriends().forEach(user -> {
      simpMessagingTemplate.convertAndSendToUser(user.getUsername(), "/feed", output);
    });
    simpMessagingTemplate.convertAndSend("/topic/posts/" + currentUser.getId(), output);
  }

  /*@MessageMapping("/feed")
  public void doMsg(@CurrentUser User currentUser, @Payload String message) {
    System.out.println(message);
  }*/
}