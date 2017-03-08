package ee.potatonet.oauth.google;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.web.RedirectStrategy;

public class GoogleOAuth2ClientAuthenticationProcessingFilter extends OAuth2ClientAuthenticationProcessingFilter {

  private final RedirectStrategy redirectStrategy;

  public GoogleOAuth2ClientAuthenticationProcessingFilter(String defaultFilterProcessesUrl, RedirectStrategy redirectStrategy) {
    super(defaultFilterProcessesUrl);
    this.redirectStrategy = redirectStrategy;
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException failed) throws IOException, ServletException {
    if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
      super.unsuccessfulAuthentication(request, response, failed);
    }
    else { //google registration failed, don't log out
      redirectStrategy.sendRedirect(
          request, response,
          String.format("/settings?failureoauth=%s", failed.getMessage())
      );
    }
  }
}
