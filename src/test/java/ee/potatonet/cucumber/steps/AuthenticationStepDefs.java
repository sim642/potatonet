package ee.potatonet.cucumber.steps;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import cucumber.api.java.en.Given;
import ee.potatonet.cucumber.config.SpringCucumberSteps;

@SpringCucumberSteps
public class AuthenticationStepDefs {

  @Given("^user is anonymous$")
  public void userIsAnonymous() throws Throwable {
    SecurityContextHolder.getContext().setAuthentication(
        new AnonymousAuthenticationToken(
            "anonymous", "anonymous",
            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"))
    );
  }
}
