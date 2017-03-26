package ee.potatonet.banklink.payment;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;

import ee.potatonet.banklink.AbstractBanklinkRequest;
import ee.potatonet.banklink.Banklink;
import ee.potatonet.banklink.BanklinkUtils;

public class PaymentRequest extends AbstractBanklinkRequest {

  protected Payment payment;

  public PaymentRequest(Banklink banklink, Payment payment) {
    super("1011", banklink);
    setPayment(payment);
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
    setLang(payment.getLanguage());
  }

  @Override
  protected LinkedHashMap<String, String> getParams() {
    LinkedHashMap<String, String> params = new LinkedHashMap<>();

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

    return params;
  }
}
