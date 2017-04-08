package ee.potatonet.cucumber.config;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.gargoylesoftware.htmlunit.WebClient;

import ee.potatonet.PotatonetApplication;

@Configuration
@Import(PotatonetApplication.class)
public class CucumberFeatureTestConfiguration {
  @Autowired
  private WebApplicationContext context;

  @Bean
  public WebClient webClient() {
    return MockMvcWebClientBuilder
        .webAppContextSetup(context, springSecurity())
        .build();
  }
}
