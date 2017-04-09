package ee.potatonet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sitemap")
public class SitemapController {

  @GetMapping
  public String doGet(Model model) {
    return "sitemap";
  }
}
