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
      WebElement post = new WebDriverWait(webDriver, 2).until(
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

  @Then("^page contains a div with class \"([^\"]*)\" which contains paragraph with id \"([^\"]*)\" and text \"([^\"]*)\"$")
  public void pageContainsADivWithClassWhichContainsParagraphWithIdAndText(String divClass, String paragraphId, String paragraphText) throws Throwable {
    try {
      WebElement div = new WebDriverWait(webDriver, 2).until(
          ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, '" + divClass + "')]//p[@id='" + paragraphId + "' and contains(.,'" + paragraphText + "')]"))
      );

      assertNotNull(div);
    }
    catch (TimeoutException e) {
      fail();
    }
  }

  @Then("^page contains a div with class \"([^\"]*)\" which contains span with text \"([^\"]*)\"$")
  public void pageContainsADivWithClassWhichContainsSpanWithText(String divClass, String spanText) throws Throwable {
    try {
      WebElement div = new WebDriverWait(webDriver, 2).until(
          ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, '" + divClass + "')]//span[contains(.,'" + spanText + "')]"))
      );

      assertNotNull(div);
    }
    catch (TimeoutException e) {
      fail();
    }
  }

  @Then("^page contains a comment with text \"([^\"]*)\"$")
  public void pageContainsCommentWithText(String commentText) throws Throwable {
    try {
      WebElement div = new WebDriverWait(webDriver, 2).until(
          ExpectedConditions.presenceOfElementLocated(By.xpath("//li[contains(@class, 'media-comment')][@data-comment-id]//span[contains(., '" + commentText +"')]"))
      );

      assertNotNull(div);
    }
    catch (TimeoutException e) {
      fail();
    }
  }

  @Then("^page contains a post with text \"([^\"]*)\" and (\\d+) likes$")
  public void pageContainsAPostWithTextAndLike(String postContent, int likeCount) throws Throwable {
    try {
      WebElement post = new WebDriverWait(webDriver, 2).until(
          ExpectedConditions.presenceOfElementLocated(
              By.xpath("//div" +
                  "[contains(@class, 'panel-post')][.//div[contains(@class, 'media-body')]//p[text()='" + postContent + "']]" +
                  "//button[contains(@class, 'like-btn')]//span[contains(@class, 'label')][text()=" + likeCount + "]")
          )
      );

      assertNotNull(post);
    }
    catch (TimeoutException e) {
      fail();
    }
  }
}

