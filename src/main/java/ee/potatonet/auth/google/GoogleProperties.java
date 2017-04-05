package ee.potatonet.auth.google;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "google")
public class GoogleProperties {

  private String authScope;
  private String accessTokenUri;
  private String userAuthorizationUri;
  private String authorizationCode;
  private String authorizationUrl;
  private String redirectUrl;
  private String preestablishedRedirectUrl;
  private String clientId;
  private String clientSecret;

  public String getAuthScope() {
    return authScope;
  }

  public void setAuthScope(String authScope) {
    this.authScope = authScope;
  }

  public String getAccessTokenUri() {
    return accessTokenUri;
  }

  public void setAccessTokenUri(String accessTokenUri) {
    this.accessTokenUri = accessTokenUri;
  }

  public String getUserAuthorizationUri() {
    return userAuthorizationUri;
  }

  public void setUserAuthorizationUri(String userAuthorizationUri) {
    this.userAuthorizationUri = userAuthorizationUri;
  }

  public String getAuthorizationCode() {
    return authorizationCode;
  }

  public void setAuthorizationCode(String authorizationCode) {
    this.authorizationCode = authorizationCode;
  }

  public String getAuthorizationUrl() {
    return authorizationUrl;
  }

  public void setAuthorizationUrl(String authorizationUrl) {
    this.authorizationUrl = authorizationUrl;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public String getPreestablishedRedirectUrl() {
    return preestablishedRedirectUrl;
  }

  public void setPreestablishedRedirectUrl(String preestablishedRedirectUrl) {
    this.preestablishedRedirectUrl = preestablishedRedirectUrl;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }
}
