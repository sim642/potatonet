package ee.potatonet.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    model.addAttribute("user", userRepository.findOne(currentUser.getId()));

    return "profile";
  }

  @PostMapping(value = "/profile/add/{id}")
  public String doGet(@CurrentUser User currentUser, @PathVariable("id") long id, Model model) {
    currentUser = userRepository.findOne(currentUser.getId());
    User user = userRepository.findOne(id);
    user.getIncomingFriendRequests().add(currentUser);
    userRepository.save(user);
    userRepository.save(currentUser);

    return "redirect:/profile/" + id;
  }
}
