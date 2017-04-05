package ee.potatonet.auth.eid;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import ee.potatonet.data.model.User;
import ee.potatonet.data.service.UserService;

@Service
public class EIDAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

  private static final List<GrantedAuthority> AUTHORITIES = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_EID");

  private final UserService userService;

  @Autowired
  public EIDAuthenticationUserDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
    EIDDetails eidDetails = (EIDDetails) token.getPrincipal();

    User user = userService.findOneByEidEmail(eidDetails.getEmail());
    if (user == null) {
      user = userService.save(new User(eidDetails));
    }

    user.setAuthorities(AUTHORITIES);
    return user;
  }
}
