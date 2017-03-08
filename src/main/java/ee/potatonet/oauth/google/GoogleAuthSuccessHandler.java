package ee.potatonet.oauth.google;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;


public class GoogleAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  public GoogleAuthSuccessHandler(RedirectStrategy redirectStrategy) {
    super();
    setRedirectStrategy(redirectStrategy);
  }

  @Override
  protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    List<String> authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    String targetUrl = "/";
    if (authorities.contains("ROLE_GOOGLE_REGISTER")) {
      targetUrl = "/settings?successoauth";
    }

    if (response.isCommitted()) {
      logger.debug("Response has already been committed. Unable to redirect to "
          + targetUrl);
      return;
    }

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
