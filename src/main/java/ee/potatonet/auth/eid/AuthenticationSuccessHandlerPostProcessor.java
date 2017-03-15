package ee.potatonet.auth.eid;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;

public class AuthenticationSuccessHandlerPostProcessor implements ObjectPostProcessor<X509AuthenticationFilter> {
  private AuthenticationSuccessHandler authenticationSuccessHandler;

  public AuthenticationSuccessHandlerPostProcessor(AuthenticationSuccessHandler authenticationSuccessHandler) {
    this.authenticationSuccessHandler = authenticationSuccessHandler;
  }

  @Override
  public <O extends X509AuthenticationFilter> O postProcess(O object) {
    object.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    return object;
  }
}
