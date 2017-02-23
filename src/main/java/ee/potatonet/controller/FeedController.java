package ee.potatonet.controller;


import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.context.ExpressionContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.expression.ThymeleafEvaluationContext;

import ee.potatonet.data.Post;
import ee.potatonet.data.User;
import ee.potatonet.data.repos.PostRepository;

@Controller
public class FeedController {

  private final PostRepository postRepository;

  private final SimpMessagingTemplate simpMessagingTemplate;
  private final SpringTemplateEngine springTemplateEngine;

  @Autowired
  public FeedController(PostRepository postRepository, SimpMessagingTemplate simpMessagingTemplate, SpringTemplateEngine springTemplateEngine) {
    this.postRepository = postRepository;
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.springTemplateEngine = springTemplateEngine;
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

  @Autowired
  private ApplicationContext applicationContext;

  @MessageMapping("/fromSockJSClient")
  public void handlePost(@CurrentUser User currentUser, Principal principal) {
    simpMessagingTemplate.convertAndSend("/feed/websocket", "{\"content\": \"stuff\"}");

    User user = (User) ((Authentication) principal).getPrincipal();

    ExpressionContext context = new ExpressionContext(springTemplateEngine.getConfiguration());
    context.setVariable("postInfo", new Post(user, "test"));
    final ThymeleafEvaluationContext evaluationContext =
        new ThymeleafEvaluationContext(applicationContext, null);
    context.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);
    String output = springTemplateEngine.process("common", Collections.singleton("post"), context);
    System.out.println(output);
  }
  
}