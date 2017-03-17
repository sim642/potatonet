package ee.potatonet.controller;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
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
import com.springcryptoutils.core.signature.Signer;

@Controller
@RequestMapping("/banklink")
public class BanklinkController {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .append(DateTimeFormatter.ISO_LOCAL_DATE)
      .appendLiteral('T')
      .appendValue(HOUR_OF_DAY, 2)
      .appendLiteral(':')
      .appendValue(MINUTE_OF_HOUR, 2)
      .appendLiteral(':')
      .appendValue(SECOND_OF_MINUTE, 2)
      .appendPattern("xx")
      .parseStrict()
      .toFormatter();

  @Autowired
  private Signer signer;

  @GetMapping
  public String doGet(Model model, CsrfToken csrfToken) {
    UriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentRequestUri().queryParam(csrfToken.getParameterName(), csrfToken.getToken());

    Map<String, String> params = new LinkedHashMap<>();
    params.put("VK_SERVICE", "1011");
    params.put("VK_VERSION", "008");
    params.put("VK_SND_ID", "uid100010");
    params.put("VK_STAMP", "123456");
    params.put("VK_AMOUNT", "1.50");
    params.put("VK_CURR", "EUR");
    params.put("VK_ACC", "EE661600001234567890");
    params.put("VK_NAME", "Potatonet");
    params.put("VK_REF", getReference("123456"));
    params.put("VK_MSG", "Raha!");
    params.put("VK_RETURN", uriComponentsBuilder.cloneBuilder().queryParam("return").toUriString());
    params.put("VK_CANCEL", uriComponentsBuilder.cloneBuilder().queryParam("cancel").toUriString());
    params.put("VK_DATETIME", ZonedDateTime.now().format(DATE_TIME_FORMATTER));

    params.put("VK_MAC", getMac(params));
    params.put("VK_ENCODING", "UTF-8");
    params.put("VK_LANG", "ENG");

    model.addAttribute("vk_params", params);
    return "banklink";
  }

  private String getMac(Map<String, String> params) {
    StringBuilder sb = new StringBuilder();
    for (String value : params.values()) {
      sb.append(String.format("%03d", value.length()));
      sb.append(value);
    }

    return Base64.encodeBase64String(signer.sign(sb.toString().getBytes(StandardCharsets.UTF_8)));
  }

  @PostMapping
  public String doPost(@RequestParam Map<String, String> params, Model model, CsrfToken csrfToken) {
    model.addAttribute("vk_params2", params);
    return doGet(model, csrfToken);
  }

  private static String getReference(String number) {
    return number + get731(number);
  }

  private static int get731(String number) {
    int[] weights = {7, 3, 1};
    int sum = 0;
    for (int i = number.length() - 1, j = 0; i >= 0; i--, j++) {
      sum += Character.digit(number.charAt(i), 10) * weights[j % weights.length];
    }
    return ((int) Math.ceil(sum / 10.0)) * 10 - sum;
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
