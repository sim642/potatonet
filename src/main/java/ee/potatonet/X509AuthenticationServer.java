package ee.potatonet;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ee.potatonet.data.User;
import ee.potatonet.data.repos.UserRepository;
import ee.potatonet.eid.AuthenticationSuccessHandlerPostProcessor;
import ee.potatonet.eid.EIDDetails;
import ee.potatonet.eid.EIDDetailsX509PrincipalExtractor;
import ee.potatonet.eid.PrincipalExtractorPostProcessor;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class X509AuthenticationServer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedirectStrategy redirectStrategy;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/img/**", "/js/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(emailUserDetailsService())
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public UserDetailsService emailUserDetailsService() {
        return email -> {
            System.out.println(email);

            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_EMAIL");

            User exisitingUser = userRepository.findOneByEstMail(email);
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
            .antMatchers("/").permitAll()
            .anyRequest().authenticated();
        http.x509()
            .withObjectPostProcessor(new PrincipalExtractorPostProcessor(eidDetailsX509PrincipalExtractor()))
            .withObjectPostProcessor(new AuthenticationSuccessHandlerPostProcessor(authenticationSuccessHandler()))
            .authenticationUserDetailsService(authenticationUserDetailsService());
        http.formLogin()
            .loginPage("/")
            .usernameParameter("email")
            .passwordParameter("password")
            .loginProcessingUrl("/")
            .successHandler(authenticationSuccessHandler())
            .permitAll();
        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .permitAll();
    }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/feed");
        successHandler.setAlwaysUseDefaultTargetUrl(false);
        successHandler.setRedirectStrategy(redirectStrategy);
        return successHandler;
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

            User exisitingUser = userRepository.findOneByEstMail(eidDetails.getEmail());
            if (exisitingUser != null) {
                System.out.println("User logged in: " + exisitingUser);
                exisitingUser.setAuthorities(authorities);

                return exisitingUser;
            } else {
                User newUser = new User(eidDetails);
                newUser.setAuthorities(authorities);

                System.out.println("Created user: " + newUser);
                userRepository.save(newUser);
                return newUser;
            }
        };
    }
}