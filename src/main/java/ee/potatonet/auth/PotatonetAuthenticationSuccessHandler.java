package ee.potatonet.auth;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import ee.potatonet.data.User;

@Component
public class PotatonetAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private final LocaleResolver localeResolver;

  @Autowired
  public PotatonetAuthenticationSuccessHandler(LocaleResolver localeResolver, RedirectStrategy redirectStrategy) {
    this.localeResolver = localeResolver;

    setDefaultTargetUrl("/");
    setAlwaysUseDefaultTargetUrl(false);
    setRedirectStrategy(redirectStrategy);
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
    super.onAuthenticationSuccess(request, response, authentication);
    localeResolver.setLocale(request, response, ((User) authentication.getPrincipal()).getLanguage().getLocale());
  }
}
