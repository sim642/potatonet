package ee.potatonet.auth.google;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class GoogleAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    ServletUriComponentsBuilder uri = ServletUriComponentsBuilder.fromContextPath(request);

    if (authentication != null && authentication.isAuthenticated()) {
      uri.path("settings");
    }
    else {
      uri.path("login");
    }

    uri.queryParam("failureoauth", exception.getMessage());

    getRedirectStrategy().sendRedirect(request, response, uri.toUriString());
  }
}
