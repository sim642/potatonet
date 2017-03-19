package ee.potatonet.banklink;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.potatonet.banklink.pangalinknet.PangalinknetBanklinkRegistrar;
import ee.potatonet.banklink.properties.PropertiesBanklinkRegistrar;

@Service
public class BanklinkService {

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

  private final BanklinkRegistry registry;
  private final BanklinkProperties banklinkProperties;

  @Autowired
  public BanklinkService(BanklinkProperties banklinkProperties) {
    registry = new DefaultBanklinkRegistry();
    this.banklinkProperties = banklinkProperties;
  }

  @PostConstruct
  public void postConstruct() {
    new PropertiesBanklinkRegistrar(banklinkProperties.getProperties()).registerBanklinks(registry);
    new PangalinknetBanklinkRegistrar(banklinkProperties.getPangalinknet()).registerBanklinks(registry);
  }

  public Collection<String> getBanklinkNames() {
    return registry.getBanklinkNames();
  }

  public BanklinkParams getPaymentParams(String banklinkName, Payment payment) {
    Banklink banklink = registry.getBanklink(banklinkName);

    Map<String, String> params = new LinkedHashMap<>();
    params.put("VK_SERVICE", "1011");
    params.put("VK_VERSION", "008");
    params.put("VK_SND_ID", banklink.getClientId());
    params.put("VK_STAMP", payment.getStamp());
    params.put("VK_AMOUNT", payment.getAmount());
    params.put("VK_CURR", payment.getCurrency());
    params.put("VK_ACC", banklink.getAccountNumber());
    params.put("VK_NAME", banklink.getAccountName());
    params.put("VK_REF", getReference(payment.getStamp()));
    params.put("VK_MSG", payment.getMessage());
    params.put("VK_RETURN", payment.getReturnUrl());
    params.put("VK_CANCEL", payment.getCancelUrl());
    params.put("VK_DATETIME", ZonedDateTime.now().format(DATE_TIME_FORMATTER));

    params.put("VK_MAC", getMac(banklink, params));
    params.put("VK_ENCODING", "UTF-8");
    params.put("VK_LANG", payment.getLanguage());

    BanklinkParams banklinkParams = new BanklinkParams();
    banklinkParams.setParams(params);
    banklinkParams.setUrl(banklink.getUrl());

    return banklinkParams;
  }

  // http://www.pangaliit.ee/et/arveldused/viitenumber
  private static String getReference(String stamp) {
    return stamp + get731(stamp);
  }

  private static int get731(String number) {
    int[] weights = {7, 3, 1};
    int sum = 0;
    for (int i = number.length() - 1, j = 0; i >= 0; i--, j++) {
      sum += Character.digit(number.charAt(i), 10) * weights[j % weights.length];
    }
    return ((int) Math.ceil(sum / 10.0)) * 10 - sum;
  }

  private String getMac(Banklink banklink, Map<String, String> params) {
    StringBuilder sb = new StringBuilder();
    for (String value : params.values()) {
      sb.append(String.format("%03d", value.length()));
      sb.append(value);
    }

    try {
      Signature signature = Signature.getInstance("SHA1withRSA");
      signature.initSign(banklink.getPrivateKey());
      signature.update(sb.toString().getBytes(StandardCharsets.UTF_8));
      return Base64.encodeBase64String(signature.sign());
    }
    catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  public static class BanklinkParams {

    private Map<String, String> params;
    private String url;

    public Map<String, String> getParams() {
      return params;
    }

    public void setParams(Map<String, String> params) {
      this.params = params;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}
