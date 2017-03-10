package ee.potatonet.oauth.google;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class GoogleLoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String url;

    if (authentication != null && authentication.isAuthenticated()) {
      url = String.format("/settings?failureoauth=%s", exception.getMessage());
    }
    else {
      url = String.format("/login?failureoauth=%s", exception.getMessage());
    }

    getRedirectStrategy().sendRedirect(request, response, url);
  }
}
