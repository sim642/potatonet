package ee.potatonet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SettingsController {

  @RequestMapping(value = "/settings", method = RequestMethod.GET)
  public String doGet(Model model) {
    // TODO: Add logic
    return "settings";
  }

}
