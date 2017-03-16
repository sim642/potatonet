package ee.potatonet.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ee.potatonet.auth.PotatonetAuthenticationSuccessHandler;
import ee.potatonet.auth.eid.AuthenticationSuccessHandlerPostProcessor;
import ee.potatonet.auth.eid.EIDAuthenticationUserDetailsService;
import ee.potatonet.auth.eid.EIDDetailsX509PrincipalExtractor;
import ee.potatonet.auth.eid.PrincipalExtractorPostProcessor;
import ee.potatonet.auth.email.EmailUserDetailsService;
import ee.potatonet.auth.google.GoogleHttpConfigurer;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private PotatonetAuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired
  private GoogleHttpConfigurer<HttpSecurity> googleHttpConfigurer;

  @Autowired
  private EmailUserDetailsService emailUserDetailsService;

  @Autowired
  private EIDAuthenticationUserDetailsService eidAuthenticationUserDetailsService;


  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**", "/img/**", "/js/**", "/favicon.ico");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(emailUserDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/login").permitAll()
        .antMatchers("/login/google/pre").anonymous()
        .anyRequest().authenticated();
    http.x509()
        .withObjectPostProcessor(new PrincipalExtractorPostProcessor(new EIDDetailsX509PrincipalExtractor()))
        .authenticationUserDetailsService(eidAuthenticationUserDetailsService)
        .withObjectPostProcessor(new AuthenticationSuccessHandlerPostProcessor(authenticationSuccessHandler));
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
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}