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
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import ee.potatonet.X509AuthenticationServer;

@Configuration
@EnableOAuth2Client
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class OAuth2SecurityConfiguration extends X509AuthenticationServer {

  private final Environment env;
  private final OAuth2ClientContextFilter contextFilter;
  private final AccessTokenRequest accessTokenRequest;
  private final GoogleLoginAccessAuthenticationConverter loginAuthenticationConverter;
  private final GoogleRegisterAccessAuthenticationConverter registerAuthenticationConverter;

  @Autowired
  public OAuth2SecurityConfiguration(Environment env, OAuth2ClientContextFilter contextFilter, AccessTokenRequest accessTokenRequest, GoogleLoginAccessAuthenticationConverter loginAuthenticationConverter, GoogleRegisterAccessAuthenticationConverter registerAuthenticationConverter) {
    this.env = env;
    this.contextFilter = contextFilter;
    this.accessTokenRequest = accessTokenRequest;
    this.loginAuthenticationConverter = loginAuthenticationConverter;
    this.registerAuthenticationConverter = registerAuthenticationConverter;
  }

  @Bean
  @Scope("session")
  public OAuth2ProtectedResourceDetails googleLoginResource() {
    AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();

    details.setId("google-login");
    details.setClientId(env.getProperty("oauth.google.login.client.id"));
    details.setClientSecret(env.getProperty("oauth.google.login.client.secret"));
    details.setTokenName(env.getProperty("oauth.google.login.authorization.code"));
    details.setUserAuthorizationUri(env.getProperty("oauth.google.login.userAuthorizationUri"));
    details.setAccessTokenUri(env.getProperty("oauth.google.login.accessTokenUri"));
    details.setPreEstablishedRedirectUri(env.getProperty("oauth.google.login.preestablished.redirect.url"));
    details.setUseCurrentUri(false);

    details.setScope(new ArrayList<>(
            Arrays.asList(
                env.getProperty("oauth.google.login.auth.scope").split(","))
        )
    );

    details.setAuthenticationScheme(AuthenticationScheme.query);
    details.setClientAuthenticationScheme(AuthenticationScheme.form);

    return details;
  }

  @Bean
  @Scope("session")
  public OAuth2ProtectedResourceDetails googleRegisterResource() {
    AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();

    details.setId("google-register");
    details.setClientId(env.getProperty("oauth.google.register.client.id"));
    details.setClientSecret(env.getProperty("oauth.google.register.client.secret"));
    details.setTokenName(env.getProperty("oauth.google.register.authorization.code"));
    details.setUserAuthorizationUri(env.getProperty("oauth.google.register.userAuthorizationUri"));
    details.setAccessTokenUri(env.getProperty("oauth.google.register.accessTokenUri"));
    details.setPreEstablishedRedirectUri(env.getProperty("oauth.google.register.preestablished.redirect.url"));
    details.setUseCurrentUri(false);

    details.setScope(new ArrayList<>(
            Arrays.asList(
                env.getProperty("oauth.google.register.auth.scope").split(","))
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
        .addFilterBefore(googleLoginAuthenticationProcessingFilter(), FilterSecurityInterceptor.class)
        .addFilterBefore(googleRegisterAuthenticationProcessingFilter(), FilterSecurityInterceptor.class);
  }

  @Bean
  public OAuth2ClientAuthenticationProcessingFilter googleLoginAuthenticationProcessingFilter() {
    OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(env.getProperty("oauth.google.login-redirect-url"));
    filter.setRestTemplate(googleLoginRestTemplate());
    filter.setTokenServices(loginTokenServices());

    return filter;
  }

  @Bean
  public OAuth2ClientAuthenticationProcessingFilter googleRegisterAuthenticationProcessingFilter() {
    OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(env.getProperty("oauth.google.register-redirect-url"));
    filter.setRestTemplate(googleRegisterRestTemplate());
    filter.setTokenServices(registerTokenServices());

    return filter;
  }

  @Bean
  public RemoteTokenServices loginTokenServices() {
    DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
    tokenConverter.setUserTokenConverter(loginAuthenticationConverter);

    GoogleRemoteTokenServices tokenServices = new GoogleRemoteTokenServices();
    tokenServices.setCheckTokenEndpointUrl("https://www.googleapis.com/oauth2/v1/tokeninfo");
    tokenServices.setClientId(env.getProperty("oauth.google.login.client.id"));
    tokenServices.setClientSecret(env.getProperty("oauth.google.login.client.secret"));
    tokenServices.setAccessTokenConverter(tokenConverter);
    tokenServices.setRestTemplate(googleLoginRestTemplate());

    return tokenServices;
  }

  @Bean
  public RemoteTokenServices registerTokenServices() {
    DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
    tokenConverter.setUserTokenConverter(registerAuthenticationConverter);

    GoogleRemoteTokenServices tokenServices = new GoogleRemoteTokenServices();
    tokenServices.setCheckTokenEndpointUrl("https://www.googleapis.com/oauth2/v1/tokeninfo");
    tokenServices.setClientId(env.getProperty("oauth.google.register.client.id"));
    tokenServices.setClientSecret(env.getProperty("oauth.google.register.client.secret"));
    tokenServices.setAccessTokenConverter(tokenConverter);
    tokenServices.setRestTemplate(googleRegisterRestTemplate());

    return tokenServices;
  }

  @Bean
  @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public OAuth2RestTemplate googleLoginRestTemplate() {
    return new OAuth2RestTemplate(googleLoginResource(), new DefaultOAuth2ClientContext(accessTokenRequest));
  }

  @Bean
  @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public OAuth2RestTemplate googleRegisterRestTemplate() {
    return new OAuth2RestTemplate(googleRegisterResource(), new DefaultOAuth2ClientContext(accessTokenRequest));
  }
}
