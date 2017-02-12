package ee.potatonet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String doGet(Model model) {
    return "login";
  }

  @RequestMapping(value = "/login_eid")
  public String loginEID() {
    return "redirect:https://localhost:8443/feed";
  }
}
