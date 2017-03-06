package ee.potatonet.controller;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;

import ee.potatonet.oauth.google.DisabledAuthenticationToken;

@Controller
public class GoogleLoginController {
  @GetMapping("/google")
  public String login() {
    DisabledAuthenticationToken preAuthentication = new DisabledAuthenticationToken(
        RequestContextHolder.currentRequestAttributes().getSessionId(),
        "N/A",
        AuthorityUtils.createAuthorityList("ROLE_PRE_GOOGLE")
    );

    SecurityContextHolder.getContext().setAuthentication(preAuthentication);

    return "redirect:/googleLogin";
  }
}
