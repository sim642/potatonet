package ee.potatonet.oauth.google;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;

@Component
public class GoogleAccessAuthenticationConverter extends DefaultUserAuthenticationConverter {

  private final MessageSource messageSource;
  private final LocaleResolver localeResolver;

  @Autowired
  public GoogleAccessAuthenticationConverter(UserService userService, MessageSource messageSource,
                                             HttpServletRequest req, LocaleResolver localeResolver) {
    this.localeResolver = localeResolver;
    this.messageSource = messageSource;

    setUserDetailsService(googleId -> {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
        throw new UsernameNotFoundException(messageSource.getMessage("oauth.problem", null, localeResolver.resolveLocale(req)));
      }
      else if (authentication.isAuthenticated()) {
        return register(googleId, authentication, userService, req);
      }
      else {
        return login(googleId, authentication, userService, req);
      }
    });
  }

  private User login(String googleId, Authentication authentication, UserService userService, HttpServletRequest req) throws UsernameNotFoundException {
    User currentUser;
    if (authentication instanceof DisabledAuthenticationToken && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PRE_GOOGLE"))) { //ensure the request is coming from /google
      currentUser = userService.findOneByGoogleId(googleId);
      if (currentUser == null) {
        throw new UsernameNotFoundException(messageSource.getMessage("oauth.nouser", null, localeResolver.resolveLocale(req)));
      }
      else {
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_GOOGLE_OAUTH");
        currentUser.setAuthorities(authorities);
      }
    }
    else {
      throw new UsernameNotFoundException(messageSource.getMessage("oauth.wrongurl", null, localeResolver.resolveLocale(req)));
    }

    return currentUser;
  }

  private User register(String googleId, Authentication authentication, UserService userService, HttpServletRequest req) throws UsernameNotFoundException {
    if (authentication.getPrincipal() instanceof User) {
      User currentUser = (User) authentication.getPrincipal();
      currentUser.setGoogleId(googleId);
      userService.save(currentUser);

      List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_GOOGLE_REGISTER");
      currentUser.setAuthorities(authorities);

      return currentUser;
    }
    else {
      throw new UsernameNotFoundException(messageSource.getMessage("oauth.problem", null, localeResolver.resolveLocale(req)));
    }
  }
}