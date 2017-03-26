package ee.potatonet.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class PotatonetErrorController implements ErrorController {

  private static final String PATH = "/error";

  private final ErrorAttributes errorAttributes;

  @Autowired
  public PotatonetErrorController(ErrorAttributes errorAttributes) {
    this.errorAttributes = errorAttributes;
  }

  @Override
  public String getErrorPath() {
    return PATH;
  }

  @ModelAttribute("errorDetails")
  private ErrorDetails errorDetails(HttpServletResponse response, HttpServletRequest request) {
    return new ErrorDetails(response.getStatus(), errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), true));
  }

  @RequestMapping(PATH)
  public String doError() {
    return "error";
  }

  public static class ErrorDetails {

    public Integer status;
    public String error;
    public String message;
    public String timestamp;
    public String trace;

    public ErrorDetails(int status, Map<String, Object> errorAttributes) {
      this.status = status;
      this.error = (String) errorAttributes.get("error");
      this.message = (String) errorAttributes.get("message");
      this.timestamp = errorAttributes.get("timestamp").toString();
      this.trace = (String) errorAttributes.get("trace");
    }
  }
}
