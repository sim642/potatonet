package ee.potatonet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.potatonet.data.model.Comment;
import ee.potatonet.web.MappingUtils;

@Controller
@RequestMapping("/comments")
public class CommentsController {

  @GetMapping(value = "/{id}", headers = MappingUtils.AJAX_HEADERS)
  public String doGetAjax(@PathVariable("id") Comment comment, Model model) {
    model.addAttribute("comment", comment);
    return "common :: commentMedia";
  }
}
