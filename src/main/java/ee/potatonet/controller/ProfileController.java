package ee.potatonet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {

  @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
  public String doGet(@PathVariable("id") long id, Model model) {
    // TODO: Add logic
    return "profile";
  }
}
