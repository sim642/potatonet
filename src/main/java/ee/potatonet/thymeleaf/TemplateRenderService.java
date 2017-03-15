package ee.potatonet.thymeleaf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.ExpressionContext;
import org.thymeleaf.spring4.expression.ThymeleafEvaluationContext;

@Service
public class TemplateRenderService {
  private final ApplicationContext applicationContext;
  private final TemplateEngine templateEngine;
  private final ConversionService conversionService;

  @Autowired
  public TemplateRenderService(ApplicationContext applicationContext, TemplateEngine templateEngine, ConversionService conversionService) {
    this.applicationContext = applicationContext;
    this.templateEngine = templateEngine;
    this.conversionService = conversionService;
  }

  public String render(String template, Set<String> templateSelectors, Map<String, Object> variables) {
    ExpressionContext context = new ExpressionContext(templateEngine.getConfiguration());
    context.setVariables(new HashMap<>(variables));

    /* http://stackoverflow.com/a/41378279 */
    final ThymeleafEvaluationContext evaluationContext = new ThymeleafEvaluationContext(applicationContext, conversionService);
    context.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);

    return templateEngine.process(template, templateSelectors, context);
  }
}
