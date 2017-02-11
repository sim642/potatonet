package ee.potatonet;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.potatonet.data.User;

@Controller
public class TestController {
    @RequestMapping("/test")
    public String test(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "test";
    }
}
