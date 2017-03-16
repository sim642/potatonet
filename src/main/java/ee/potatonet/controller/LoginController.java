package ee.potatonet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ee.potatonet.data.model.User;

@Controller
public class LoginController {

  @RequestMapping(value = "/login")
  public String doGet(@CurrentUser User currentUser, Model model) {
    if (currentUser == null)
      return "login";
    else
      return "redirect:/";
  }

  @RequestMapping("/login/eid")
  @ResponseBody
  public void doGetEid() {

  }
}
