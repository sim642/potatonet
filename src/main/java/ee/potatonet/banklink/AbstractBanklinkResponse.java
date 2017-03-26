package ee.potatonet.banklink;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractBanklinkResponse {

  protected Banklink banklink;
  protected Map<String, String> rawParams;

  public AbstractBanklinkResponse(Banklink banklink, Map<String, String> rawParams) {
    this.banklink = banklink;
    this.rawParams = rawParams;
  }

  public Banklink getBanklink() {
    return banklink;
  }

  public void setBanklink(Banklink banklink) {
    this.banklink = banklink;
  }

  public Map<String, String> getRawParams() {
    return rawParams;
  }

  public void setRawParams(Map<String, String> rawParams) {
    this.rawParams = rawParams;
  }

  protected abstract List<String> getParamsOrder();

  protected List<String> getSignedParamsOrder() {
    List<String> signedParamsOrder = new ArrayList<>();
    signedParamsOrder.add("VK_SERVICE");
    signedParamsOrder.add("VK_VERSION");
    signedParamsOrder.addAll(getParamsOrder());

    return signedParamsOrder;
  }

  protected LinkedHashMap<String, String> getSignedParams() {
    LinkedHashMap<String, String> signedParams = new LinkedHashMap<>();

    for (String key : getSignedParamsOrder())
      signedParams.put(key, rawParams.get(key));

    return signedParams;
  }

  private String getMac() {
    return rawParams.get("VK_MAC");
  }

  public boolean isValid() {
    return BanklinkUtils.verifyParams(banklink, getSignedParams(), getMac());
  }
}
