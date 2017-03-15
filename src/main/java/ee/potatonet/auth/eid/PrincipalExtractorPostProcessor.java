package ee.potatonet.auth.eid;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.x509.X509PrincipalExtractor;

public class PrincipalExtractorPostProcessor implements ObjectPostProcessor<X509AuthenticationFilter> {
  private final X509PrincipalExtractor principalExtractor;

  public PrincipalExtractorPostProcessor(X509PrincipalExtractor principalExtractor) {
    this.principalExtractor = principalExtractor;
  }

  @Override
  public <O extends X509AuthenticationFilter> O postProcess(O object) {
    object.setPrincipalExtractor(principalExtractor);
    return object;
  }
}
