package ee.potatonet.banklink;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import ee.potatonet.banklink.pangalinknet.PangalinknetBanklinkRegistrar;
import ee.potatonet.banklink.properties.PropertiesBanklinkRegistrar;

@Service
public class BanklinkService {

  private final BanklinkRegistry registry;
  private final BanklinkProperties banklinkProperties;

  @Autowired
  public BanklinkService(BanklinkProperties banklinkProperties) {
    this.registry = new DefaultBanklinkRegistry();
    this.banklinkProperties = banklinkProperties;
  }

  @Bean
  public BanklinkRegistry banklinkRegistry() {
    return registry;
  }

  @PostConstruct
  public void postConstruct() {
    new PropertiesBanklinkRegistrar(banklinkProperties.getProperties()).registerBanklinks(registry);
    new PangalinknetBanklinkRegistrar(banklinkProperties.getPangalinknet()).registerBanklinks(registry);
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
    params.put("VK_REF", BanklinkUtils.getReferenceNumber(payment.getStamp()));
    params.put("VK_MSG", payment.getMessage());
    params.put("VK_RETURN", payment.getReturnUrl());
    params.put("VK_CANCEL", payment.getCancelUrl());
    params.put("VK_DATETIME", ZonedDateTime.now().format(BanklinkUtils.DATE_TIME_FORMATTER));

    params.put("VK_MAC", getMac(banklink, params));
    params.put("VK_ENCODING", "UTF-8");
    params.put("VK_LANG", payment.getLanguage());

    BanklinkParams banklinkParams = new BanklinkParams();
    banklinkParams.setParams(params);
    banklinkParams.setUrl(banklink.getUrl());

    return banklinkParams;
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
