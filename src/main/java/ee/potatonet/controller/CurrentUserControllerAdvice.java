package ee.potatonet.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ee.potatonet.data.User;

@ControllerAdvice
public class CurrentUserControllerAdvice {
  @ModelAttribute
  public void currentUserAttribute(@CurrentUser User currentUser, Model model) {
    model.addAttribute("currentUser", currentUser);
  }
}
