package ee.potatonet.cucumber.steps;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import ee.potatonet.cucumber.config.SpringCucumberSteps;

@SpringCucumberSteps
public class UrlNavigationStepDefs {

  @Autowired
  private MockMvc mockMvc;

  private ResultActions resultActions;

  @When("^user navigates to url \"([^\"]*)\"$")
  public void userNavigatesToUrl(String uri) throws Throwable {
    resultActions = mockMvc.perform(get(new URI(uri)));
  }

  @Then("^user is redirected to \"([^\"]*)\"$")
  public void userIsRedirectedTo(String redirectUrl) throws Throwable {
    resultActions
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(redirectUrl));
  }
}
