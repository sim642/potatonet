package ee.potatonet.cucumber.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import ee.potatonet.TestUtils;
import ee.potatonet.auth.eid.EIDCodeDetails;
import ee.potatonet.auth.eid.EIDDetails;
import ee.potatonet.cucumber.config.CucumberTestState;
import ee.potatonet.data.model.User;
import ee.potatonet.data.service.UserService;

public class UserStepDefs {

  @Autowired
  private UserService userService;

  @Autowired
  private WebDriver webDriver;

  @Autowired
  private CucumberTestState testState;

  @And("^I have a friend$")
  public void iHaveAFriend() throws Throwable {
    User friend = userService.save(TestUtils.generateUser());
    User currentUser = testState.getMyUser();

    userService.addFriendship(currentUser, friend);
    testState.setFriend(friend);
  }

  @Given("^exists user with email \"([^\"]*)\" and pass \"([^\"]*)\"$")
  public void existsUserWithNameAndPass(String email, String password) throws Throwable {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    User user = userService.findOneByEidEmail(email);
    if (user == null) {
      user = new User(new EIDDetails(new EIDCodeDetails("30101010000"), email, email, email));
    }

    user.setPassword(passwordEncoder.encode(password));

    userService.save(user);
  }

  @Given("^I am anonymous$")
  public void userIsAnonymous() throws Throwable {
    webDriver.get("https://localhost:8443/logout");
  }

  @And("^I log in with email \"([^\"]*)\" and pass \"([^\"]*)\"$")
  public void userLogsInWithUserAndPass(String username, String password) throws Throwable {
    assertThat(webDriver.getCurrentUrl(), containsString("/login"));

    WebElement userNameInput = webDriver.findElement(
        By.xpath("//form//input[@type='email']")
    );
    WebElement passwordInput = webDriver.findElement(
        By.xpath("//form//input[@type='password']")
    );
    WebElement submitButton = webDriver.findElement(
        By.xpath("//form//button[@type='submit']")
    );

    userNameInput.sendKeys(username);
    passwordInput.sendKeys(password);
    submitButton.submit();
  }

  @Given("^I am authenticated$")
  public void userIsAuthenticated() throws Throwable {
    User me = userService.save(TestUtils.generateUser());
    testState.setMyUser(me);

    webDriver.get("https://localhost:8443/login");

    userLogsInWithUserAndPass(me.getUsername(), "password");
  }
}
