package ee.potatonet.controller;

import ee.potatonet.data.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PeopleController {

  @RequestMapping(value = "/people", method = RequestMethod.GET)
  public String doGet() {
    return "people";
  }
}
