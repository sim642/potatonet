package ee.potatonet.cucumber.steps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import ee.potatonet.auth.eid.EIDCodeDetails;
import ee.potatonet.auth.eid.EIDDetails;
import ee.potatonet.cucumber.config.CucumberTestContext;
import ee.potatonet.cucumber.config.SpringCucumberSteps;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.UserService;

@SpringCucumberSteps
public class AuthenticationStepDefs {

  @Autowired
  private UserService userService;

  @Autowired
  private WebClient webClient;

  @Autowired
  private CucumberTestContext context;

  @Given("^user is anonymous$")
  public void userIsAnonymous() throws Throwable {
    SecurityContextHolder.getContext().setAuthentication(
        new AnonymousAuthenticationToken(
            "anonymous", "anonymous",
            AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"))
    );
  }

  @And("^user logs in with name \"([^\"]*)\" and pass \"([^\"]*)\"$")
  public void userLogsInWithUserAndPass(String username, String password) throws Throwable {
    assertThat(context.getCurrentPage().getUrl().getPath(), is("/login"));

    HtmlForm form = context.getCurrentPage().getFormByName("login-form");

    HtmlTextInput userNameInput = form.getInputByName("email");
    HtmlPasswordInput passwordInput = form.getInputByName("password");
    HtmlButton submit = form.getOneHtmlElementByAttribute("button", "type", "submit");

    userNameInput.setValueAttribute(username);
    passwordInput.setValueAttribute(password);

    context.setCurrentPage(submit.click());
  }

  @And("^exists user with name \"([^\"]*)\" and pass \"([^\"]*)\"$")
  public void existsUserWithNameAndPass(String userName, String password) throws Throwable {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    User user = new User(new EIDDetails(new EIDCodeDetails("30101010000"), userName, userName, userName));
    user.setPassword(passwordEncoder.encode(password));

    userService.save(user);
  }
}
