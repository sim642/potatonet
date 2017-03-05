package ee.potatonet.oauth.google;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

@Component
public class GoogleLoginAccessAuthenticationConverter extends DefaultUserAuthenticationConverter {

  @Autowired
  public GoogleLoginAccessAuthenticationConverter(UserService userService) {
    setUserDetailsService(googleId -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      User currentUser;

      if (authentication != null && authentication instanceof PreAuthenticatedAuthenticationToken && authentication.getAuthorities().contains("ROLE_PRE_GOOGLE")) { //ensure the request is coming from /google
        currentUser = userService.findOneByGoogleId(googleId);
        if (currentUser == null) {
          throw new UsernameNotFoundException("No user registered with your google account was found.");
        } else {
          List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_GOOGLE_OAUTH");
          currentUser.setAuthorities(authorities);
        }
      }
      else {
        throw new UsernameNotFoundException("No user registered with your google account was found.");
      }

      return currentUser;
    });
  }
}