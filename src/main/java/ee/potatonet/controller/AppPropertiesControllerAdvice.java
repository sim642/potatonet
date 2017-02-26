package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import ee.potatonet.AppProperties;

@ControllerAdvice
public class AppPropertiesControllerAdvice {
  @Autowired
  private AppProperties appProperties;

  @ModelAttribute
  public void appAttribute(Model model) {
    model.addAttribute("app", appProperties);
  }
}
