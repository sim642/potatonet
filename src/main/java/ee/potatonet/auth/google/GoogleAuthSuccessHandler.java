package ee.potatonet.auth.google;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;


public class GoogleAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final AuthenticationSuccessHandler delegate;

  public GoogleAuthSuccessHandler(RedirectStrategy redirectStrategy, AuthenticationSuccessHandler delegate) {
    super();
    this.delegate = delegate;
    setRedirectStrategy(redirectStrategy);
  }

  @Override
  protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    List<String> authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());

    if (authorities.contains("ROLE_GOOGLE_REGISTER")) {
      getRedirectStrategy().sendRedirect(request, response, "/settings?successoauth");
    } else {
      delegate.onAuthenticationSuccess(request,response,authentication);
    }
  }
}
