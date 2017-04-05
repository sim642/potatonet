package ee.potatonet.auth.eid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;

public class EIDHttpConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<EIDHttpConfigurer<H>, H> {

  private X509AuthenticationFilter x509AuthenticationFilter;
  private AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService;
  private AuthenticationSuccessHandler authenticationSuccessHandler;

  public EIDHttpConfigurer<H> authenticationUserDetailsService(AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService) {
    this.authenticationUserDetailsService = authenticationUserDetailsService;
    return this;
  }

  public EIDHttpConfigurer<H> authenticationSuccessHandler(AuthenticationSuccessHandler authenticationSuccessHandler) {
    this.authenticationSuccessHandler = authenticationSuccessHandler;
    return this;
  }

  // Copied from X509Configurer
  @Override
  public void init(H http) throws Exception {
    PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
    authenticationProvider.setPreAuthenticatedUserDetailsService(authenticationUserDetailsService);

    http
        .authenticationProvider(authenticationProvider)
        .setSharedObject(AuthenticationEntryPoint.class, new Http403ForbiddenEntryPoint());
  }

  // Copied from X509Configurer
  @Override
  public void configure(H http) throws Exception {
    X509AuthenticationFilter filter = getFilter(http
        .getSharedObject(AuthenticationManager.class));
    http.addFilter(filter);
  }

  // Copied from X509Configurer
  private X509AuthenticationFilter getFilter(AuthenticationManager authenticationManager) {
    if (x509AuthenticationFilter == null) {
      x509AuthenticationFilter = new X509AuthenticationFilter();
      x509AuthenticationFilter.setAuthenticationManager(authenticationManager);
      x509AuthenticationFilter.setPrincipalExtractor(new EIDDetailsX509PrincipalExtractor());
      x509AuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
      x509AuthenticationFilter = postProcess(x509AuthenticationFilter);
    }

    return x509AuthenticationFilter;
  }
}
