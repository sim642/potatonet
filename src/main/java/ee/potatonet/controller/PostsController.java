package ee.potatonet.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.potatonet.controller.advice.CurrentUser;
import ee.potatonet.data.model.Comment;
import ee.potatonet.data.model.Post;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.PostService;
import ee.potatonet.web.MappingUtils;

@Controller
@RequestMapping("/posts")
public class PostsController {

  @Autowired
  private PostService postService;

  @GetMapping(value = "/{id}", headers = MappingUtils.NON_AJAX_HEADERS)
  public String doGet(@PathVariable("id") Post post, Model model) {
    model.addAttribute("post", post);
    return "post";
  }

  @GetMapping(value = "/{id}", headers = MappingUtils.AJAX_HEADERS)
  public String doGetAjax(@PathVariable("id") Post post, Model model) {
    model.addAttribute("post", post);
    return "common :: postPanel";
  }

  @PostMapping(value = "/{id}/comments", headers = MappingUtils.NON_AJAX_HEADERS)
  public String doPostComments(@PathVariable("id") Post post, @CurrentUser User currentUser, @Valid @ModelAttribute Comment comment) {
    comment.setUser(currentUser);
    postService.saveCommentToPost(comment, post);
    return "redirect:/posts/" + post.getId();
  }
}
