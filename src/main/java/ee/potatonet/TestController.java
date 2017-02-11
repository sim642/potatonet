package ee.potatonet;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
public class TestController {
    @RequestMapping("/test")
    public String test(Model model, Principal principal) {
        UserDetails user = (UserDetails) ((Authentication) principal).getPrincipal();
        model.addAttribute("name", user.getUsername());
        return "test";
    }

    @RequestMapping("/test2")
    public String test2(Model model) {
        return "login";
    }
}
