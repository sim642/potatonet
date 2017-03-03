package ee.potatonet.oauth.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import ee.potatonet.data.User;
import ee.potatonet.data.UserService;

@Component
public class GoogleAccessAuthenticationConverter extends DefaultUserAuthenticationConverter {

  @Autowired
  public GoogleAccessAuthenticationConverter(UserService userService) {
    setUserDetailsService(googleId -> {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      User currentUser;
      if (principal == null) {
        currentUser = userService.findOneByGoogleId(googleId);
      }
      else {
        currentUser = (User) principal;
        currentUser.setGoogleId(googleId);
        userService.save(currentUser);
      }

      return currentUser;
    });
  }

}