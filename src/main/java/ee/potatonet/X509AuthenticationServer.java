package ee.potatonet;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import ee.potatonet.data.User;
import ee.potatonet.data.repos.UserRepository;
import ee.potatonet.eid.EIDDetails;
import ee.potatonet.eid.EIDDetailsX509PrincipalExtractor;
import ee.potatonet.eid.PrincipalExtractorPostProcessor;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class X509AuthenticationServer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/img/**")
                .antMatchers(HttpMethod.GET, "/");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .x509()
                .withObjectPostProcessor(new PrincipalExtractorPostProcessor(eidDetailsX509PrincipalExtractor()))
                .authenticationUserDetailsService(authenticationUserDetailsService());
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