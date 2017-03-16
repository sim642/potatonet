package ee.potatonet.auth.email;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ee.potatonet.data.model.User;
import ee.potatonet.data.service.UserService;

@Service
public class EmailUserDetailsService implements UserDetailsService {

  private static final List<GrantedAuthority> AUTHORITIES = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_EMAIL");

  private final UserService userService;

  @Autowired
  public EmailUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userService.findOneByEidEmail(email);
    if (user != null) {
      user.setAuthorities(AUTHORITIES);
      return user;
    }
    else {
      throw new UsernameNotFoundException(email);
    }
  }
}
