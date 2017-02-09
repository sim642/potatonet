package ee.potatonet;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class TestController {
    @RequestMapping("/test")
    public String test(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model, Principal principal) {
        UserDetails user = (UserDetails) ((Authentication) principal).getPrincipal();
        model.addAttribute("name", user.getUsername());
        return "test";
    }
}
