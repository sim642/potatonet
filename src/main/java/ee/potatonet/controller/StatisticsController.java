package ee.potatonet.controller;

import ee.potatonet.data.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {

  private final PostService postService;

  @Autowired
  public StatisticsController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/statistics")
  public String doGet(Model model) {
    return "statistics";
  }
}
