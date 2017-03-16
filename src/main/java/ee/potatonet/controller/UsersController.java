package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.potatonet.data.model.User;
import ee.potatonet.data.service.UserService;

@Controller
@RequestMapping("/users")
public class UsersController {

  private final UserService userService;

  @Autowired
  public UsersController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public String doGet(@CurrentUser User user, Model model) {
    model.addAttribute("users", userService.findAll());
    return "users";
  }
}
