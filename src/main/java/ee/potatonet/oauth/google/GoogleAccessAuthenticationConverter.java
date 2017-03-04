package ee.potatonet.oauth.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

      User currentUser;
      if (authentication == null) {
        currentUser = userService.findOneByGoogleId(googleId);
        if (currentUser == null) {
          throw new UsernameNotFoundException("No user registered with your google account was found.");
        }
      }
      else {
        currentUser = (User) authentication.getPrincipal();
        currentUser.setGoogleId(googleId);
        userService.save(currentUser);
      }


      return currentUser;
    });
  }

}