package ee.potatonet.controller;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.potatonet.data.User;
import ee.potatonet.data.UserService;
import ee.potatonet.data.repos.UserRepository;

@Controller
public class SettingsController {

  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Autowired
  public SettingsController(PasswordEncoder passwordEncoder, UserService userService) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
  }

  @RequestMapping(value = "/settings", method = RequestMethod.GET)
  public String doGet(Model model) {
    model.addAttribute("passwordSettings", new PasswordSettings());
    return "settings";
  }

  @PostMapping("/settings")
  public String doPost(@ModelAttribute @Valid PasswordSettings passwordSettings, BindingResult bindingResult, @CurrentUser User currentUser) { // WTF spring magic: http://stackoverflow.com/a/29075342
    if (bindingResult.hasErrors()) {
      return "settings";
    }

    currentUser = userService.find(currentUser);
    currentUser.setPassword(passwordEncoder.encode(passwordSettings.getPassword()));
    userService.save(currentUser);

    return "redirect:/settings?success";
  }

  public static class PasswordSettings {
    @NotNull
    @Size(min = 8)
    private String password;

    private String passwordConfirm;

    @AssertTrue
    public boolean isMatching() {
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
