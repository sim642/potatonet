package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ee.potatonet.data.User;
import ee.potatonet.data.repos.UserRepository;

@Controller
public class SettingsController {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserRepository userRepository;

  @RequestMapping(value = "/settings", method = RequestMethod.GET)
  public String doGet(Model model) {
    // TODO: Add logic
    return "settings";
  }

  @PostMapping("/settings")
  public String doPost(@CurrentUser User currentUser, @RequestParam("inputPassword") String password, @RequestParam("inputPassword2") String password2) {
    if (!password.equals(password2)) {
      return "redirect:/settings?error";
    }

    currentUser.setPassword(passwordEncoder.encode(password));
    userRepository.save(currentUser);

    return "redirect:/settings?success";
  }
}
