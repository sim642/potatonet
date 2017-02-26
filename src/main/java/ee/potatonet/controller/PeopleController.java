package ee.potatonet.controller;

import ee.potatonet.data.User;
import ee.potatonet.data.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PeopleController {

  @Autowired
  private UserService userService;

  @RequestMapping(value = "/people", method = RequestMethod.GET)
  public String doGet(@CurrentUser User user, Model model) {
    model.addAttribute("users", userService.findAll());
    return "people";
  }
}
