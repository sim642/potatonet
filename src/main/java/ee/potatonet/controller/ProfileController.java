package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.potatonet.data.User;
import ee.potatonet.data.repos.UserRepository;

@Controller
public class ProfileController {

  private final UserRepository userRepository;

  @Autowired
  public ProfileController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
  public String doGet(@PathVariable("id") long id, Model model) {
    model.addAttribute("user", userRepository.findOne(id));

    return "profile";
  }

  @RequestMapping(value = "/profile", method = RequestMethod.GET)
  public String doGet(@CurrentUser User currentUser, Model model) {
    model.addAttribute("user", currentUser);

    return "profile";
  }
}
