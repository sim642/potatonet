package ee.potatonet.oauth.google;


import java.util.ArrayList;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import ee.potatonet.X509AuthenticationServer;

@Configuration
@EnableOAuth2Client
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class OAuth2SecurityConfiguration extends X509AuthenticationServer {

  private final OAuth2ClientContextFilter contextFilter;
  private final AccessTokenRequest accessTokenRequest;
  private final GoogleAccessAuthenticationConverter authenticationConverter;
  private final RedirectStrategy redirectStrategy;
  private final GoogleProperties googleProperties;

  @Autowired
  public OAuth2SecurityConfiguration(
      OAuth2ClientContextFilter contextFilter,
      AccessTokenRequest accessTokenRequest,
      GoogleAccessAuthenticationConverter authenticationConverter,
      RedirectStrategy redirectStrategy,
      GoogleProperties googleProperties) {
    this.contextFilter = contextFilter;
    this.accessTokenRequest = accessTokenRequest;
    this.authenticationConverter = authenticationConverter;
    this.redirectStrategy = redirectStrategy;
    this.googleProperties = googleProperties;
  }

  @Bean
  @Scope("session")
  public OAuth2ProtectedResourceDetails googleResource() {
    AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();

    details.setId("google-login");
    details.setClientId(googleProperties.getClientId());
    details.setClientSecret(googleProperties.getClientSecret());
    details.setTokenName(googleProperties.getAuthorizationCode());
    details.setUserAuthorizationUri(googleProperties.getUserAuthorizationUri());
    details.setAccessTokenUri(googleProperties.getAccessTokenUri());
    details.setPreEstablishedRedirectUri(googleProperties.getPreestablishedRedirectUrl());
    details.setUseCurrentUri(false);

    details.setScope(new ArrayList<>(
            Arrays.asList(googleProperties.getAuthScope().split(","))
        )
    );

    details.setAuthenticationScheme(AuthenticationScheme.query);
    details.setClientAuthenticationScheme(AuthenticationScheme.form);

    return details;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    http.addFilterAfter(contextFilter, ExceptionTranslationFilter.class)
        .addFilterBefore(googleAuthenticationProcessingFilter(), FilterSecurityInterceptor.class);
  }

  @Bean
  public OAuth2ClientAuthenticationProcessingFilter googleAuthenticationProcessingFilter() {
    GoogleOAuth2ClientAuthenticationProcessingFilter filter =
        new GoogleOAuth2ClientAuthenticationProcessingFilter(googleProperties.getRedirectUrl());
    filter.setRestTemplate(googleRestTemplate());
    filter.setTokenServices(googleTokenServices());
    filter.setAuthenticationSuccessHandler(new GoogleAuthSuccessHandler(redirectStrategy, authenticationSuccessHandler()));
    filter.setAuthenticationFailureHandler(new GoogleLoginAuthenticationFailureHandler());

    return filter;
  }

  @Bean
  public RemoteTokenServices googleTokenServices() {
    DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
    tokenConverter.setUserTokenConverter(authenticationConverter);

    GoogleRemoteTokenServices tokenServices = new GoogleRemoteTokenServices();
    tokenServices.setCheckTokenEndpointUrl("https://www.googleapis.com/oauth2/v1/tokeninfo");
    tokenServices.setClientId(googleProperties.getClientId());
    tokenServices.setClientSecret(googleProperties.getClientSecret());
    tokenServices.setAccessTokenConverter(tokenConverter);
    tokenServices.setRestTemplate(googleRestTemplate());

    return tokenServices;
  }

  @Bean
  @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public OAuth2RestTemplate googleRestTemplate() {
    return new OAuth2RestTemplate(googleResource(), new DefaultOAuth2ClientContext(accessTokenRequest));
  }
}