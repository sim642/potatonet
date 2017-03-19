package ee.potatonet.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public String doGet(Model model, CsrfToken csrfToken) {
    UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri().queryParam(csrfToken.getParameterName(), csrfToken.getToken());

    Payment payment = new Payment();
    payment.setStamp("123456");
    payment.setAmount("1.50");
    payment.setCurrency("EUR");
    payment.setMessage("Raha!");
    payment.setReturnUrl(uriComponentsBuilder.cloneBuilder().queryParam("return").toUriString());
    payment.setCancelUrl(uriComponentsBuilder.cloneBuilder().queryParam("cancel").toUriString());
    payment.setLanguage("ENG");

    model.addAttribute("vk_params", banklinkService.getPaymentParams("testpank", payment));
    return "banklink";
  }

  @PostMapping
  public String doPost(@RequestParam Map<String, String> params, Model model, CsrfToken csrfToken) {
    model.addAttribute("vk_params2", params);
    return doGet(model, csrfToken);
  }

  private static String encode(Map<String, String> params) {
    try {
      StringBuilder sb = new StringBuilder();
      for (Map.Entry<String, String> entry : params.entrySet()) {
        if (sb.length() > 0)
          sb.append("&");

        sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
        sb.append("=");
        sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
      }
      return sb.toString();
    }
    catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
