package ee.potatonet.cucumber.steps;

import static org.junit.Assert.assertTrue;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.html.HtmlParagraph;

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
    List<WebElement> posts = webDriver.findElements(
        By.xpath("//div[contains(@class, 'panel-post')]//div[contains(@class, 'media-body')]//p")
    );

    boolean postExists = posts.stream()
        .map(WebElement::getText)
        .anyMatch(s -> s.equals(postContent));

    assertTrue(postExists);
  }
}

