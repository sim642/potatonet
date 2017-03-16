package ee.potatonet.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.web.servlet.LocaleResolver;

import ee.potatonet.data.model.Language;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.UserService;

@Controller
public class SettingsController {

  private final PasswordEncoder passwordEncoder;
  private final UserService userService;
  private final LocaleResolver localeResolver;

  @Autowired
  public SettingsController(PasswordEncoder passwordEncoder, UserService userService, LocaleResolver localeResolver) {
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.localeResolver = localeResolver;
  }

  @RequestMapping(value = "/settings", method = RequestMethod.GET)
  public String doGet(Model model, @CurrentUser User currentUser) {
    model.addAttribute("passwordSettings", new PasswordSettings());
    model.addAttribute("languageSettings", new LanguageSettings(userService.find(currentUser).getLanguage()));
    return "settings";
  }

  @PostMapping("/settings/password")
  public String doPost(@ModelAttribute @Valid PasswordSettings passwordSettings, BindingResult bindingResult, @CurrentUser User currentUser, Model model) { // WTF spring magic: http://stackoverflow.com/a/29075342
    if (bindingResult.hasErrors()) {
      model.addAttribute("languageSettings", new LanguageSettings(userService.find(currentUser).getLanguage()));
      return "settings";
    }

    currentUser = userService.find(currentUser);
    currentUser.setPassword(passwordEncoder.encode(passwordSettings.getPassword()));
    userService.save(currentUser);

    return "redirect:/settings?password_success";
  }

  @PostMapping("/settings/locale")
  public String doPost(@ModelAttribute LanguageSettings languageSettings, @CurrentUser User currentUser,
                       HttpServletRequest req, HttpServletResponse resp) {
    currentUser = userService.find(currentUser);
    currentUser.setLanguage(languageSettings.getLanguage());
    userService.save(currentUser);

    localeResolver.setLocale(req, resp, languageSettings.getLanguage().getLocale());
    return "redirect:/settings?locale_success";
  }


  public static class LanguageSettings {
    private Language language;

    public LanguageSettings() {
    }

    public LanguageSettings(Language language) {
      this.language = language;
    }

    public Language getLanguage() {
      return language;
    }

    public void setLanguage(Language language) {
      this.language = language;
    }

    public List<Language> getAllLanguages() {
      return Arrays.asList(Language.values());
    }
  }

  public static class PasswordSettings {
    @NotNull
    @Size(min = 8, message = "{settings.password.validation.size}")
    private String password;

    private String passwordConfirm;

    @AssertTrue(message = "{settings.password.validation.matching}")
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
