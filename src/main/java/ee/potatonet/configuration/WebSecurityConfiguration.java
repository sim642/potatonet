package ee.potatonet.configuration;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ee.potatonet.auth.PotatonetAuthenticationSuccessHandler;
import ee.potatonet.auth.eid.AuthenticationSuccessHandlerPostProcessor;
import ee.potatonet.auth.eid.EIDDetails;
import ee.potatonet.auth.eid.EIDDetailsX509PrincipalExtractor;
import ee.potatonet.auth.eid.PrincipalExtractorPostProcessor;
import ee.potatonet.auth.google.GoogleHttpConfigurer;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.UserService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserService userService;

  @Autowired
  private PotatonetAuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired
  private GoogleHttpConfigurer<HttpSecurity> googleHttpConfigurer;


  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**", "/img/**", "/js/**", "/favicon.ico");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(emailUserDetailsService())
        .passwordEncoder(passwordEncoder());
  }


  @Bean
  public UserDetailsService emailUserDetailsService() {
    return email -> {
      System.out.println(email);

      List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_EMAIL");

      User exisitingUser = userService.findOneByEidEmail(email);
      if (exisitingUser != null) {
        System.out.println("User logged in: " + exisitingUser);
        exisitingUser.setAuthorities(authorities);

        return exisitingUser;
      }
      else {
        throw new UsernameNotFoundException(email);
      }
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/login").permitAll()
        .antMatchers("/login/google/pre").anonymous()
        .anyRequest().authenticated();
    http.x509()
        .withObjectPostProcessor(new PrincipalExtractorPostProcessor(eidDetailsX509PrincipalExtractor()))
        .withObjectPostProcessor(new AuthenticationSuccessHandlerPostProcessor(authenticationSuccessHandler))
        .authenticationUserDetailsService(authenticationUserDetailsService());
    http.formLogin()
        .loginPage("/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .loginProcessingUrl("/login")
        .successHandler(authenticationSuccessHandler)
        .permitAll();
    http.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/login")
        .permitAll();
    http.apply(googleHttpConfigurer);
  }

  @Bean
  public EIDDetailsX509PrincipalExtractor eidDetailsX509PrincipalExtractor() {
    return new EIDDetailsX509PrincipalExtractor();
  }

  @Bean
  public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> authenticationUserDetailsService() {
    return token -> {
      EIDDetails eidDetails = (EIDDetails) token.getPrincipal();
      System.out.println(eidDetails);

      List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_EID");

      User exisitingUser = userService.findOneByEidEmail(eidDetails.getEmail());
      if (exisitingUser != null) {
        System.out.println("User logged in: " + exisitingUser);
        exisitingUser.setAuthorities(authorities);

        return exisitingUser;
      }
      else {
        User newUser = new User(eidDetails);
        newUser.setAuthorities(authorities);

        System.out.println("Created user: " + newUser);
        return userService.save(newUser);
      }
    };
  }

}