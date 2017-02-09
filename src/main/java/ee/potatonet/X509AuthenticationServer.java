package ee.potatonet;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import sun.security.x509.RFC822Name;

import java.io.IOException;
import java.security.cert.CertificateParsingException;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class X509AuthenticationServer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*http.authorizeRequests().anyRequest().authenticated()
                .and()
                .x509()
                //.subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .subjectPrincipalRegex("serialNumber=(\\d{11})")
                .userDetailsService(userDetailsService());*/
        http.authorizeRequests()
                .antMatchers("/test")
                    .authenticated()
                    .and()
                    .x509()
                        .withObjectPostProcessor(new X509PostProcessor())
                        //.subjectPrincipalRegex("(?:serialNumber|SERIALNUMBER)=(\\d{11})")
                        .userDetailsService(userDetailsService());
    }

    private static class X509PostProcessor implements ObjectPostProcessor<X509AuthenticationFilter> {

        @Override
        public <O extends X509AuthenticationFilter> O postProcess(O object) {
            object.setPrincipalExtractor(cert -> {
                try {
                    return new RFC822Name((String) cert.getSubjectAlternativeNames().iterator().next().get(1)).getName();
                }
                catch (CertificateParsingException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
            return object;
        }
    }

    /*
    Issued to: serialNumber=39603032788,givenName=SIMMO,SN=SAAN,CN="SAAN,SIMMO,39603032788",OU=authentication,O=ESTEID,C=EE
  Serial Number: 0D:34:3D:0C:7C:B5:38:48:52:52:92:E9:D0:B9:BB:F2
  Valid from E 07 okt   2013 13:54:33 EET to N 04 okt   2018 23:59:59 EET
  Certificate Key Usage: Signing,Key Encipherment,Data Encipherment
  Email: simmo.saan@eesti.ee
Issued by: E=pki@sk.ee,CN=ESTEID-SK 2011,O=AS Sertifitseerimiskeskus,C=EE
Stored in: SAAN,SIMMO,39603032788 (PIN1)
     */

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                System.out.println(username);
                return new User(username, "",
                        AuthorityUtils
                                .commaSeparatedStringToAuthorityList("ROLE_EID"));
                //throw new UsernameNotFoundException(username);
            }
        };
    }
}