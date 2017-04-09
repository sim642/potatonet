package ee.potatonet.cucumber.steps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ee.potatonet.cucumber.config.CucumberTestState;
import ee.potatonet.cucumber.config.SpringCucumberSteps;

@SpringCucumberSteps
public class UrlNavigationStepDefs {

  @Autowired
  private WebDriver webDriver;

  @Autowired
  private CucumberTestState testState;

  @When("^I navigate to url \"([^\"]*)\"$")
  public void userNavigatesToUrl(String url) throws Throwable {
    webDriver.get(url);
  }

  @Then("^I am redirected to \"([^\"]*)\"$")
  public void userIsRedirectedTo(String redirectUrl) throws Throwable {
    assertThat(webDriver.getCurrentUrl(), is(redirectUrl));
  }

  @When("^I navigate to my user view$")
  public void iNavigateToMyUserView() throws Throwable {
    Long id = testState.getMyUser().getId();
    userNavigatesToUrl("https://localhost:8443/users/" + id);
  }

  @When("^I navigate to my friend user view$")
  public void iNavigateToMyFriendUserView() throws Throwable {
    Long id = testState.getFriend().getId();
    userNavigatesToUrl("https://localhost:8443/users/" + id);
  }
}
