package ee.potatonet.cucumber.steps;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.api.PendingException;
import cucumber.api.java.en.Then;
import ee.potatonet.cucumber.config.CucumberTestState;
import ee.potatonet.cucumber.config.SpringCucumberSteps;

@SpringCucumberSteps
public class PageContentStepDef {

  @Autowired
  private CucumberTestState testState;

  @Autowired
  private WebDriver webDriver;

  @Then("^page contains a post with text \"([^\"]*)\"$")
  public void pageContainsAPostWithText(String postContent) throws Throwable {
    try {
      WebElement post = new WebDriverWait(webDriver, 10).until(
          ExpectedConditions.presenceOfElementLocated(
              By.xpath("//div[contains(@class, 'panel-post')]//div[contains(@class, 'media-body')]//p[text()='" + postContent + "']")
          )
      );

      assertNotNull(post);
    }
    catch (TimeoutException e) {
      fail();
    }
  }

  @Then("^page contains a user panel with name \"([^\"]*)\" with a button of class \"([^\"]*)\"$")
  public void pageContainsAUserPanelWithNameWithAButtonOfClass(String userName, String btnClass) throws Throwable {
    try {
      WebElement button = new WebDriverWait(webDriver, 2).until(
          ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'panel-user')][.//h3[contains(.,'" + userName + "')]]//button[contains(@class, '" + btnClass + "')]"))
      );

      assertNotNull(button);
    }
    catch (TimeoutException e) {
      fail();
    }
  }

  @Then("^page contains a user panel with name \"([^\"]*)\"$")
  public void pageContainsAUserPanelWithName(String userName) throws Throwable {
    try {
      WebElement div = new WebDriverWait(webDriver, 2).until(
          ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'panel-user')][.//h3[contains(.,'" + userName + "')]]"))
      );

      assertNotNull(div);
    }
    catch (TimeoutException e) {
      fail();
    }
  }

  @Then("^page doesn't contain a user panel with name \"([^\"]*)\"$")
  public void pageDoesnTContainAUserPanelWithName(String userName) throws Throwable {
    try {
      WebElement div = new WebDriverWait(webDriver, 2).until(
          ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'panel-user')][.//h3[contains(.,'" + userName + "')]]"))
      );

      fail();
    }
    catch (TimeoutException e) {
      // success
    }
  }
}

