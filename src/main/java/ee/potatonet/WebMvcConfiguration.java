package ee.potatonet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

  private final MessageSource messageSource;

  @Autowired
  public WebMvcConfiguration(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Bean
  public LocalValidatorFactoryBean validator() { // Spring autoconfiguration is weird, return type Validator does not work
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setValidationMessageSource(messageSource);
    return validator;
  }

  @Override
  public Validator getValidator() {
    return validator();
  }
}
