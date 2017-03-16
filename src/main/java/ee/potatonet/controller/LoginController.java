package ee.potatonet.controller;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;

import ee.potatonet.auth.google.DisabledAuthenticationToken;
import ee.potatonet.controller.advice.CurrentUser;
import ee.potatonet.data.model.User;

@Controller
@RequestMapping("/login")
public class LoginController {

  @GetMapping
  public String doGet(@CurrentUser User currentUser, Model model) {
    if (currentUser == null)
      return "login";
    else
      return "redirect:/";
  }

  @GetMapping("/eid")
  @ResponseBody
  public void doGetEid() {

  }

  @GetMapping("/google/pre")
  public String doGetGooglePre() {
    DisabledAuthenticationToken preAuthentication = new DisabledAuthenticationToken(
        RequestContextHolder.currentRequestAttributes().getSessionId(),
        "N/A",
        AuthorityUtils.createAuthorityList("ROLE_PRE_GOOGLE")
    );

    SecurityContextHolder.getContext().setAuthentication(preAuthentication);

    return "redirect:/login/google";
  }
}
