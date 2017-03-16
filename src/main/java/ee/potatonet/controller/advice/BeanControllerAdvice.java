package ee.potatonet.controller.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class BeanControllerAdvice {

  private final ApplicationContext applicationContext;

  @Autowired
  public BeanControllerAdvice(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @ModelAttribute
  public void hasBeanAttributes(Model model) {
    model.addAttribute("hasBuildProperties", applicationContext.containsBean("buildProperties"));
    model.addAttribute("hasGitProperties", applicationContext.containsBean("gitProperties"));
  }
}
