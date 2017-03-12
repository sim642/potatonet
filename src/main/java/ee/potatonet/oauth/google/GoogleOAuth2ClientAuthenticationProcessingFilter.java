package ee.potatonet.oauth.google;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;

public class GoogleOAuth2ClientAuthenticationProcessingFilter extends OAuth2ClientAuthenticationProcessingFilter {

  public GoogleOAuth2ClientAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
    super(defaultFilterProcessesUrl);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
    if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) { //google registration failed, don't log out
      getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
    else {
      super.unsuccessfulAuthentication(request, response, failed);
    }
  }
}
