package ee.potatonet.banklink;

import javax.annotation.PostConstruct;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import ee.potatonet.banklink.pangalinknet.PangalinknetBanklinkRegistrar;
import ee.potatonet.banklink.payment.Payment;
import ee.potatonet.banklink.payment.PaymentFailureResponse;
import ee.potatonet.banklink.payment.PaymentRequest;
import ee.potatonet.banklink.payment.PaymentSuccessResponse;
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

    PaymentRequest paymentRequest = new PaymentRequest(banklink, payment);
    BanklinkParams banklinkParams = new BanklinkParams();
    banklinkParams.setParams(paymentRequest.getCompleteParams());
    banklinkParams.setUrl(banklink.getUrl());

    return banklinkParams;
  }

  public AbstractBanklinkResponse getResponse(String banklinkName, Map<String, String> rawParams) {
    Banklink banklink = registry.getBanklink(banklinkName);

    String service = rawParams.get("VK_SERVICE");
    switch (service) {
      case "1111":
        return new PaymentSuccessResponse(banklink, rawParams);

      case "1911":
        return new PaymentFailureResponse(banklink, rawParams);

      default:
        throw new RuntimeException("unknown response service");
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
