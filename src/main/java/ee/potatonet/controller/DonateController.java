package ee.potatonet.controller;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import ee.potatonet.banklink.BanklinkService;
import ee.potatonet.banklink.Payment;

@Controller
@RequestMapping("/donate")
public class DonateController {

  private final BanklinkService banklinkService;

  private final MessageSource messageSource;
  private final LocaleResolver localeResolver;

  @Autowired
  public DonateController(BanklinkService banklinkService, MessageSource messageSource, LocaleResolver localeResolver) {
    this.banklinkService = banklinkService;
    this.messageSource = messageSource;
    this.localeResolver = localeResolver;
  }

  @GetMapping
  public String doGet() {
    return "donate";
  }

  @PostMapping
  public String doPost(@RequestParam Map<String, String> params, Model model) {
    model.addAttribute("vk_params", params);
    return "donate";
  }

  @GetMapping("/params")
  @ResponseBody
  public BanklinkService.BanklinkParams doGetParams(@RequestParam("banklinkName") String banklinkName, CsrfToken csrfToken, HttpServletRequest request) {
    Locale locale = localeResolver.resolveLocale(request);

    UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/donate")
        .queryParam(csrfToken.getParameterName(), csrfToken.getToken());

    Payment payment = new Payment();
    payment.setStamp("123456");
    payment.setAmount("1.00");
    payment.setCurrency("EUR");
    payment.setMessage(messageSource.getMessage("donate.message", null, locale));
    payment.setReturnUrl(uriComponentsBuilder.cloneBuilder().queryParam("return").toUriString());
    payment.setCancelUrl(uriComponentsBuilder.cloneBuilder().queryParam("cancel").toUriString());
    payment.setLanguage(messageSource.getMessage("banklink.language", null, locale));

    return banklinkService.getPaymentParams(banklinkName, payment);
  }
}
