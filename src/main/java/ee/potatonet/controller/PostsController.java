package ee.potatonet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.potatonet.data.model.Post;
import ee.potatonet.web.MappingUtils;

@Controller
@RequestMapping("/posts")
public class PostsController {

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
}
