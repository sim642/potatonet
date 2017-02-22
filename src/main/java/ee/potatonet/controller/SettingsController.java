package ee.potatonet.controller;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    model.addAttribute("passwordSettings", new PasswordSettings());
    return "settings";
  }

  @PostMapping("/settings")
  public String doPost(@ModelAttribute PasswordSettings passwordSettings, @CurrentUser User currentUser) {
    if (!passwordSettings.areMatching()) {
      return "redirect:/settings?error";
    }

    currentUser.setPassword(passwordEncoder.encode(passwordSettings.getPassword()));
    userRepository.save(currentUser);

    return "redirect:/settings?success";
  }

  public static class PasswordSettings {
    private String password;
    private String passwordConfirm;

    public boolean areMatching() {
      return Objects.equals(password, passwordConfirm);
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public String getPasswordConfirm() {
      return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
      this.passwordConfirm = passwordConfirm;
    }
  }
}
