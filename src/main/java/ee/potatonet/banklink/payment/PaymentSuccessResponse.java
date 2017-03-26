package ee.potatonet.banklink.payment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ee.potatonet.banklink.AbstractBanklinkResponse;
import ee.potatonet.banklink.Banklink;

public class PaymentSuccessResponse extends AbstractBanklinkResponse {

  public PaymentSuccessResponse(Banklink banklink, Map<String, String> rawParams) {
    super(banklink, rawParams);
  }

  @Override
  protected List<String> getParamsOrder() {
    return Arrays.asList(
        "VK_SND_ID",
        "VK_REC_ID",
        "VK_STAMP",
        "VK_T_NO",
        "VK_AMOUNT",
        "VK_CURR",
        "VK_REC_ACC",
        "VK_REC_NAME",
        "VK_SND_ACC",
        "VK_SND_NAME",
        "VK_REF",
        "VK_MSG",
        "VK_T_DATETIME"
        );
  }
}
