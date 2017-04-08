package ee.potatonet.cucumber.steps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;


import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.WebClient;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ee.potatonet.cucumber.config.CucumberTestContext;
import ee.potatonet.cucumber.config.SpringCucumberSteps;

@SpringCucumberSteps
public class UrlNavigationStepDefs {

  @Autowired
  private WebClient webClient;

  @Autowired
  private CucumberTestContext context;

  @When("^user navigates to url \"([^\"]*)\"$")
  public void userNavigatesToUrl(String url) throws Throwable {
    context.setCurrentPage(webClient.getPage(url));
  }

  @Then("^user is redirected to \"([^\"]*)\"$")
  public void userIsRedirectedTo(String redirectUrl) throws Throwable {
    assertThat(context.getCurrentPage().getUrl().toExternalForm(), is(redirectUrl));
  }
}
