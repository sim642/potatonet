package ee.potatonet.oauth.google;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

@Configuration
@EnableOAuth2Client
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(100)
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Autowired
  private Environment env;
  @Autowired
  OAuth2ClientContextFilter contextFilter;

  @Resource
  @Qualifier("accessTokenRequest")
  private AccessTokenRequest accessTokenRequest;

  @Bean
  public OAuth2ProtectedResourceDetails googleResource() {
    AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();

    details.setClientId(env.getProperty("oauth.google.client.id"));
    details.setClientSecret(env.getProperty("oauth.google.client.secret"));
    details.setTokenName(env.getProperty("oauth.google.authorization.code"));
    details.setUserAuthorizationUri(env.getProperty("oauth.google.userAuthorizationUri"));
    details.setAccessTokenUri(env.getProperty("oauth.google.accessTokenUri"));
    details.setPreEstablishedRedirectUri(env.getProperty("oauth.google.preestablished.redirect.url"));

    details.setScope(new ArrayList<>(
        Arrays.asList(
            env.getProperty("oauth.google.auth.scope").split(","))
        )
    );

    details.setAuthenticationScheme(AuthenticationScheme.query);
    details.setClientAuthenticationScheme(AuthenticationScheme.form);

    return details;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterAfter(contextFilter, ExceptionTranslationFilter.class)
        .addFilterBefore(oAuth2AuthenticationProcessingFilter(), FilterSecurityInterceptor.class);
  }

  @Bean
  public OAuth2ClientAuthenticationProcessingFilter oAuth2AuthenticationProcessingFilter() {
    OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter("/googleLogin");
    filter.setRestTemplate(googleRestTemplate());
    filter.setTokenServices(tokenServices());

    return filter;
  }

  @Bean
  public RemoteTokenServices tokenServices() {
    DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
    tokenConverter.setUserTokenConverter(new GoogleAccessAuthenticationConverter());

    RemoteTokenServices tokenServices = new RemoteTokenServices();
    tokenServices.setCheckTokenEndpointUrl("https://www.googleapis.com/oauth2/v1/tokeninfo");
    tokenServices.setClientId(env.getProperty("${oauth.google.client.id}"));
    tokenServices.setClientSecret(env.getProperty("${oauth.google.client.secret}"));
    tokenServices.setAccessTokenConverter(tokenConverter);

    return tokenServices;
  }

  @Bean
  public OAuth2RestTemplate googleRestTemplate() {
    return new OAuth2RestTemplate(googleResource(), new DefaultOAuth2ClientContext(accessTokenRequest));
  }
}
