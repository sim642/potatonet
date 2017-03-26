package ee.potatonet.banklink.pangalinknet;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
class Project {

  private String id;
  private String name;

  private String clientId;
  private String paymentUrl;

  private String accountOwner;
  private String accountNr;

  private String privateKey;
  private String bankCertificate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getPaymentUrl() {
    return paymentUrl;
  }

  public void setPaymentUrl(String paymentUrl) {
    this.paymentUrl = paymentUrl;
  }

  public String getAccountOwner() {
    return accountOwner;
  }

  public void setAccountOwner(String accountOwner) {
    this.accountOwner = accountOwner;
  }

  public String getAccountNr() {
    return accountNr;
  }

  public void setAccountNr(String accountNr) {
    this.accountNr = accountNr;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getBankCertificate() {
    return bankCertificate;
  }

  public void setBankCertificate(String bankCertificate) {
    this.bankCertificate = bankCertificate;
  }
}
