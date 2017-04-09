package ee.potatonet.cucumber.config;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.web.context.WebApplicationContext;


import ee.potatonet.PotatonetApplication;

@Configuration
@Import(PotatonetApplication.class)
public class CucumberFeatureTestConfiguration {

  @Autowired
  private WebApplicationContext context;

  @Bean
  public WebDriver webClient() {
    return MockMvcHtmlUnitDriverBuilder
        .webAppContextSetup(context, springSecurity())
        .javascriptEnabled(true)
        .build();
  }
}
