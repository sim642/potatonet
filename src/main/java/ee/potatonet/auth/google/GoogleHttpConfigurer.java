package ee.potatonet.auth.google;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

public class GoogleHttpConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<GoogleHttpConfigurer<H>, H> {

  private OAuth2ClientContextFilter contextFilter;
  private OAuth2ClientAuthenticationProcessingFilter authenticationProcessingFilter;

  @Override
  public void configure(H http) throws Exception {
    http.addFilterAfter(contextFilter, ExceptionTranslationFilter.class)
        .addFilterBefore(authenticationProcessingFilter, FilterSecurityInterceptor.class);
  }

  public GoogleHttpConfigurer<H> contextFilter(OAuth2ClientContextFilter contextFilter) {
    this.contextFilter = contextFilter;
    return this;
  }

  public GoogleHttpConfigurer<H> authenticationProcessingFilter(OAuth2ClientAuthenticationProcessingFilter authenticationProcessingFilter) {
    this.authenticationProcessingFilter = authenticationProcessingFilter;
    return this;
  }
}
