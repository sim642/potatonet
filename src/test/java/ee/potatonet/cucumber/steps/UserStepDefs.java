package ee.potatonet.cucumber.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
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

  @Given("^exists user with email \"([^\"]*)\" and pass \"([^\"]*)\" and eid_code \"([^\"]*)\"$")
  public void existsUserWithEmailAndPassAndEidCode(String email, String password, String eidCode) throws Throwable {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    User user = userService.findOneByEidEmail(email);
    if (user == null) {
      user = new User(new EIDDetails(new EIDCodeDetails(eidCode), email, "", email));
    } else {
      user.setEid(new EIDDetails(new EIDCodeDetails(eidCode), user.getEid().getGivenName(), user.getEid().getSurname(), user.getEid().getEmail()));
    }

    user.setPassword(passwordEncoder.encode(password));

    userService.save(user);
  }

  @Given("^exists user with email \"([^\"]*)\" and pass \"([^\"]*)\"$")
  public void existsUserWithEmailAndPass(String email, String password) throws Throwable {
    existsUserWithEmailAndPassAndEidCode(email, password, "30101010000");
  }

  @Given("^exists user with email \"([^\"]*)\"$")
  public void existsUserWithEmail(String email) throws Throwable {
    existsUserWithEmailAndPassAndEidCode(email, "password168", "30101010000");
  }

  @Given("^exists user with email \"([^\"]*)\" and eid_code \"([^\"]*)\"$")
  public void existsUserWithEmailAndEid_code(String email, String eidCode) throws Throwable {
    existsUserWithEmailAndPassAndEidCode(email, "password168", eidCode);
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

    testState.setMyUser(userService.findOneByEidEmail(username));
  }

  @Given("^I am authenticated$")
  public void userIsAuthenticated() throws Throwable {
    User me = userService.save(TestUtils.generateUser());
    testState.setMyUser(me);

    webDriver.get("https://localhost:8443/logout");
    webDriver.get("https://localhost:8443/login");
    userLogsInWithUserAndPass(me.getUsername(), "password");
  }

  @Given("^user \"([^\"]*)\" has sent friend request to me$")
  public void userHasSentFriendRequestToMe(String email) throws Throwable {
    User me = testState.getMyUser();
    User user = userService.findOneByEidEmail(email);

    userService.addFriendRequest(user, me);
  }

  @Given("^I am not friends with user \"([^\"]*)\"$")
  public void iAmNotFriendsWithUser(String email) throws Throwable {
    User me = testState.getMyUser();
    User notFriend = userService.findOneByEidEmail(email);

    userService.removeFriends(me, notFriend);
    userService.removeFriendRequests(me, notFriend);
  }

  @Given("^I am friends with user \"([^\"]*)\"$")
  public void iAmFriendsWithUser(String email) throws Throwable {
    User me = testState.getMyUser();
    User notFriend = userService.findOneByEidEmail(email);

    userService.addFriendship(me, notFriend);
  }
}
