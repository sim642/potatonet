package ee.potatonet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.ExpressionContext;
import org.thymeleaf.spring4.expression.ThymeleafEvaluationContext;

@Service
public class TemplateRenderService {
  private final ApplicationContext applicationContext;
  private final TemplateEngine templateEngine;

  @Autowired
  public TemplateRenderService(ApplicationContext applicationContext, TemplateEngine templateEngine) {
    this.applicationContext = applicationContext;
    this.templateEngine = templateEngine;
  }

  public String render(String template, Set<String> templateSelectors, Map<String, Object> variables) {
    ExpressionContext context = new ExpressionContext(templateEngine.getConfiguration());
    context.setVariables(new HashMap<>(variables));

    /* http://stackoverflow.com/a/41378279 */
    // TODO: 23.02.17 conversion
    final ThymeleafEvaluationContext evaluationContext = new ThymeleafEvaluationContext(applicationContext, null);
    context.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);

    return templateEngine.process(template, templateSelectors, context);
  }
}
