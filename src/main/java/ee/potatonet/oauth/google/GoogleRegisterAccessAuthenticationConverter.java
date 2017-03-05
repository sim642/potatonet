package ee.potatonet.oauth.google;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

@Component
public class GoogleRegisterAccessAuthenticationConverter extends DefaultUserAuthenticationConverter {

  @Autowired
  public GoogleRegisterAccessAuthenticationConverter(UserService userService) {
    setUserDetailsService(googleId -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication != null && authentication.getPrincipal() instanceof User) {
        User currentUser = (User) authentication.getPrincipal();
        currentUser.setGoogleId(googleId);
        userService.save(currentUser);

        return currentUser;
      }
      else {
        throw new UsernameNotFoundException("Problems finding current user");
      }
    });
  }
}