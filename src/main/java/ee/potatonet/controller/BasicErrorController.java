package ee.potatonet.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BasicErrorController implements ErrorController{

  private static final String PATH = "/error";

  @Override
  public String getErrorPath() {
    return PATH;
  }

  @RequestMapping(value = PATH)
  public String doGet() {
    // TODO: Add generic error template page
    return "error";
  }
}
