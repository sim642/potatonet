package ee.potatonet.configuration;

import javax.servlet.ServletContext;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import ee.potatonet.thymeleaf.StandardFallbackLinkBuilder;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;

@Configuration
public class ThymeleafConfiguration {

  private final Collection<ITemplateResolver> templateResolvers;
  private final ServletContext servletContext;

  @Autowired
  public ThymeleafConfiguration(Collection<ITemplateResolver> templateResolvers, ServletContext servletContext) {
    this.templateResolvers = templateResolvers;
    this.servletContext = servletContext;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    templateResolvers.forEach(engine::addTemplateResolver);

    engine.addDialect(new LayoutDialect(new GroupingStrategy()));
    engine.setLinkBuilder(new StandardFallbackLinkBuilder(servletContext));
    return engine;
  }
}
