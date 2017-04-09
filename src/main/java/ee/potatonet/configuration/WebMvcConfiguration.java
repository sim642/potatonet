package ee.potatonet.configuration;

import com.github.ziplet.filter.compression.CompressingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import ee.potatonet.data.model.Language;

import javax.servlet.Filter;

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

  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(Language.EN.getLocale());
    return cookieLocaleResolver;
  }

  @Bean
  public Filter compressingFilter() {
    return new CompressingFilter();
  }
}
