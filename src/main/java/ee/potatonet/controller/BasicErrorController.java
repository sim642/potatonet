package ee.potatonet.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;

import ee.potatonet.AppProperties;

@Controller
public class BasicErrorController implements ErrorController {

  private static final String PATH = "/error";

  @Autowired
  private AppProperties appProperties;

  @Autowired
  private ErrorAttributes errorAttributes;

  @Override
  public String getErrorPath() {
    return PATH;
  }

  @RequestMapping(value = PATH)
  public String error() {
    return "error";
  }

  @ModelAttribute("errorDetails")
  private ErrorDetails errorDetails(HttpServletResponse response, HttpServletRequest request) {
    return new ErrorDetails(response.getStatus(), errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), true));
  }

  @ModelAttribute("showErrors")
  public boolean getShowErrors() {
    return appProperties.getShowErrors();
  }
}
