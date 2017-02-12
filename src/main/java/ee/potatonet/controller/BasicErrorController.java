package ee.potatonet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class BasicErrorController implements ErrorController {

  private static final String PATH = "/error";

  @Value("${app.devMode}")
  private boolean devMode;

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

  @ModelAttribute("errorJson")
  private ErrorDetails errorJSON(HttpServletResponse response, HttpServletRequest request) {
    return new ErrorDetails(response.getStatus(), errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), true));
  }

  @ModelAttribute("devMode")
  public boolean isDevMode() {
    return devMode;
  }
}
