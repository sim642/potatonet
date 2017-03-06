package ee.potatonet.oauth.google;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

@Component
public class GoogleAccessAuthenticationConverter extends DefaultUserAuthenticationConverter {

  @Autowired
  public GoogleAccessAuthenticationConverter(UserService userService) {
    setUserDetailsService(googleId -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
        throw new UsernameNotFoundException("Problem authenticating with oauth");
      }
      else if (authentication.isAuthenticated()) {
        return register(googleId, authentication, userService);
      }
      else {
        return login(googleId, authentication, userService);
      }
    });
  }

  private User login(String googleId, Authentication authentication, UserService userService) throws UsernameNotFoundException {
    User currentUser;
    if (authentication instanceof DisabledAuthenticationToken && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PRE_GOOGLE"))) { //ensure the request is coming from /google
      currentUser = userService.findOneByGoogleId(googleId);
      if (currentUser == null) {
        throw new UsernameNotFoundException("No user registered with your google account was found.");
      }
      else {
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_GOOGLE_OAUTH");
        currentUser.setAuthorities(authorities);
      }
    }
    else {
      throw new UsernameNotFoundException("This url was requested from a wrong place");
    }

    return currentUser;
  }

  private User register(String googleId, Authentication authentication, UserService userService) throws UsernameNotFoundException {
    if (authentication.getPrincipal() instanceof User) {
      User currentUser = (User) authentication.getPrincipal();
      currentUser.setGoogleId(googleId);
      userService.save(currentUser);

      return currentUser;
    }
    else {
      throw new UsernameNotFoundException("Problems finding current user");
    }
  }
}