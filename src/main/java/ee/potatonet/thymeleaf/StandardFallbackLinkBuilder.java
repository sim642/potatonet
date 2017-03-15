package ee.potatonet.thymeleaf;

import javax.servlet.ServletContext;
import java.util.Map;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.linkbuilder.StandardLinkBuilder;

public class StandardFallbackLinkBuilder extends StandardLinkBuilder {

  private final ServletContext servletContext;

  public StandardFallbackLinkBuilder(ServletContext servletContext) {
    this.servletContext = servletContext;
  }

  @Override
  protected String computeContextPath(IExpressionContext context, String base, Map<String, Object> parameters) {
    try {
      return super.computeContextPath(context, base, parameters);
    }
    catch (TemplateProcessingException e) {
      return servletContext.getContextPath();
    }
  }
}
