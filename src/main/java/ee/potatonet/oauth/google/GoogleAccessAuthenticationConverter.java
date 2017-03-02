package ee.potatonet.oauth.google;

import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import ee.potatonet.data.User;

public class GoogleAccessAuthenticationConverter extends DefaultUserAuthenticationConverter {

  public GoogleAccessAuthenticationConverter() {
    setUserDetailsService(new UserDetailsService() {
      @Override
      public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);

        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      }
    });
  }

  public Authentication extractAuthentication(Map<String, ?> map) {
    Authentication authentication = super.extractAuthentication(map);
    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), authentication.getAuthorities());
  }

}