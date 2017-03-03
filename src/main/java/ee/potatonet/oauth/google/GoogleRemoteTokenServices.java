package ee.potatonet.oauth.google;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;

/*Mostly copied from RemoteTokenServices*/
public class GoogleRemoteTokenServices extends RemoteTokenServices {

  private String checkTokenEndpointUrl;
  private String clientId;
  private String clientSecret;
  private AccessTokenConverter tokenConverter;
  private RestOperations restTemplate;

  // Copy from RemoteTokenServices, but changed Assert.state to check for issued_to and user_id instead. Also add Spring Boot required key,values to the map
  @Override
  public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
    Map<String, Object> map = requestToken(accessToken);

    if (map.containsKey("error")) {
      logger.debug("check_token returned error: " + map.get("error"));
      throw new InvalidTokenException(accessToken);
    }

    Assert.state(map.containsKey("issued_to"));
    Assert.state(map.containsKey("user_id"));

    map.put("client_id", map.get("issued_to")); // Required by Spring Boot' default AccessTokenConverter
    map.put("user_name", map.get("user_id")); // Required by Spring Boot' default AccessTokenConverter

    return tokenConverter.extractAuthentication(map);
  }

  // Copy from RemoteTokenServices, but changed tokenUrl
  private Map<String, Object> requestToken(String accessToken) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
    formData.add("access_token", accessToken);
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));

    String tokenUrl = this.checkTokenEndpointUrl + "?access_token=" + accessToken;

    return postForMap(tokenUrl, formData, headers);
  }

  // Copy from RemoteTokenServices
  private String getAuthorizationHeader(String clientId, String clientSecret) {
    if (clientId == null || clientSecret == null) {
      logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
    }

    String creds = String.format("%s:%s", clientId, clientSecret);
    try {
      return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
    }
    catch (UnsupportedEncodingException e) {
      throw new IllegalStateException("Could not convert String");
    }
  }

  // Copy from RemoteTokenServices
  private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
    if (headers.getContentType() == null) {
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }
    @SuppressWarnings("rawtypes")
    Map map = restTemplate.exchange(path, HttpMethod.POST,
        new HttpEntity<>(formData, headers), Map.class).getBody();
    @SuppressWarnings("unchecked")
    Map<String, Object> result = map;
    return result;
  }

  @Override
  public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl) {
    this.checkTokenEndpointUrl = checkTokenEndpointUrl;
  }

  @Override
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  @Override
  public void setClientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
  }

  @Override
  public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
    this.tokenConverter = accessTokenConverter;
  }

  @Override
  public void setRestTemplate(RestOperations restTemplate) {
    this.restTemplate = restTemplate;
  }
}
