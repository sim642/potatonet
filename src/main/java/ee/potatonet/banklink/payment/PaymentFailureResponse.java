package ee.potatonet.banklink.payment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import ee.potatonet.banklink.AbstractBanklinkResponse;
import ee.potatonet.banklink.Banklink;

public class PaymentFailureResponse extends AbstractBanklinkResponse {

  public PaymentFailureResponse(Banklink banklink, Map<String, String> rawParams) {
    super(banklink, rawParams);
  }

  @Override
  protected List<String> getParamsOrder() {
    return Arrays.asList(
        "VK_SND_ID",
        "VK_REC_ID",
        "VK_STAMP",
        "VK_REF",
        "VK_MSG"
        );
  }
}
