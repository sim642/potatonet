package ee.potatonet.banklink.pangalinknet;

import java.security.PrivateKey;

import ee.potatonet.banklink.AbstractBanklink;

public class PangalinknetBanklink extends AbstractBanklink {

  private PrivateKey privateKey;

  @Override
  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }
}
