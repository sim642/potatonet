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
      List<WebElement> posts = new WebDriverWait(webDriver, 2).until(
          ExpectedConditions.presenceOfAllElementsLocatedBy(
              By.xpath("//div[contains(@class, 'panel-post')]//div[contains(@class, 'media-body')]//p")
          )
      );

      boolean postExists = posts.stream()
          .map(WebElement::getText)
          .anyMatch(s -> s.equals(postContent));

      assertTrue(postExists);
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
}

