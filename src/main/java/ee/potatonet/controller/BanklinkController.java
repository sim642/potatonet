package ee.potatonet.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import ee.potatonet.banklink.BanklinkService;
import ee.potatonet.banklink.Payment;

@Controller
@RequestMapping("/banklink")
public class BanklinkController {

  @Autowired
  private BanklinkService banklinkService;

  @GetMapping
  public String doGet() {
    return "banklink";
  }

  @PostMapping
  public String doPost(@RequestParam Map<String, String> params, Model model) {
    model.addAttribute("vk_params", params);
    return "banklink";
  }

  @GetMapping("/params")
  @ResponseBody
  public BanklinkService.BanklinkParams doGetParams(@RequestParam("banklinkName") String banklinkName, CsrfToken csrfToken) {
    UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/banklink").queryParam(csrfToken.getParameterName(), csrfToken.getToken());

    Payment payment = new Payment();
    payment.setStamp("123456");
    payment.setAmount("1.50");
    payment.setCurrency("EUR");
    payment.setMessage("Raha!");
    payment.setReturnUrl(uriComponentsBuilder.cloneBuilder().queryParam("return").toUriString());
    payment.setCancelUrl(uriComponentsBuilder.cloneBuilder().queryParam("cancel").toUriString());
    payment.setLanguage("ENG");

    return banklinkService.getPaymentParams(banklinkName, payment);
  }
}
