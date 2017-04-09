package ee.potatonet.cucumber.steps;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ee.potatonet.cucumber.config.CucumberTestState;
import ee.potatonet.cucumber.config.SpringCucumberSteps;
import ee.potatonet.data.service.UserService;

@SpringCucumberSteps
public class UrlNavigationStepDefs {

  @Autowired
  private WebDriver webDriver;

  @Autowired
  private UserService userService;

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

  @When("^I click on user panel button with class \"([^\"]*)\" for user \"([^\"]*)\"$")
  public void iClickOnUserPanelButtonWithClassForUser(String btnClass, String userName) throws Throwable {
    WebElement button = new WebDriverWait(webDriver, 2).until(
        ExpectedConditions.elementToBeClickable(
            By.xpath("//div[contains(@class, 'panel-user')][.//h3[contains(.,'" + userName + "')]]//button[contains(@class, '" + btnClass + "')]")
        )
    );
    button.click();
  }

  @When("^I navigate to my friends view$")
  public void iNavigateToMyFriendsView() throws Throwable {
    Long id = testState.getMyUser().getId();
    userNavigatesToUrl("https://localhost:8443/users/" + id + "/friends");
  }

  @When("^I navigate to users \"([^\"]*)\" view$")
  public void iNavigateToUsersView(String eidEmail) throws Throwable {
    Long id = userService.findOneByEidEmail(eidEmail).getId();
    userNavigatesToUrl("https://localhost:8443/users/" + id);
  }

  @When("^I change password to \"([^\"]*)\"$")
  public void iChangePasswordTo(String newPassword) throws Throwable {
    savePassword(newPassword, newPassword);
  }

  @When("^I miswrite password confirmation$")
  public void iMiswritePasswordConfirmation() throws Throwable {
    savePassword("asdadsdasdasd", "#dvfbvbmfgbewr");
  }

  private void savePassword(String pass, String confirm) {
    assertThat(webDriver.getCurrentUrl(), containsString("/settings"));

    WebElement passwordInput = webDriver.findElement(
        By.xpath("//form//input[@id='password']")
    );
    WebElement passwordConfirmInput = webDriver.findElement(
        By.xpath("//form//input[@id='passwordConfirm']")
    );
    WebElement saveButton = webDriver.findElements(
        By.xpath("//form//button[@type='submit']")
    ).get(0);

    passwordInput.sendKeys(pass);
    passwordConfirmInput.sendKeys(confirm);
    saveButton.submit();
  }

  @When("^I switch language to \"([^\"]*)\"$")
  public void iSwitchLanguageTo(String languageName) throws Throwable {
    WebElement languageInput = webDriver.findElement(
        By.xpath("//form//input[@id='" + languageName + "']")
    );
    WebElement saveButton = webDriver.findElements(
        By.xpath("//form//button[@type='submit']")
    ).get(1);

    languageInput.click();
    saveButton.submit();
  }

  @When("^I like a post with content \"([^\"]*)\"$")
  public void iLikeAPostWithContent(String postContent) throws Throwable {
    try {
      WebElement postButton = new WebDriverWait(webDriver, 10).until(
          ExpectedConditions.presenceOfElementLocated(
              By.xpath("//div" +
                  "[contains(@class, 'panel-post')][.//div[contains(@class, 'media-body')]//p[text()='" + postContent + "']]" +
                  "//button[contains(@class, 'like-btn')]")
          )
      );

      postButton.click();
    }
    catch (TimeoutException e) {
      fail();
    }
  }
}
