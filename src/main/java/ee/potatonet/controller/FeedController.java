package ee.potatonet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FeedController {

  @RequestMapping(value = "/feed", method = RequestMethod.GET)
  public String doGet(Model model) {
    return "feed";
  }
}